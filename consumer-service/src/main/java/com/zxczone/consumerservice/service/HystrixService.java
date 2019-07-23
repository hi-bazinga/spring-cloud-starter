package com.zxczone.consumerservice.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.Future;

@Service
public class HystrixService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "hystrixFallback", commandKey = "testHystrix")
    public String hystrix() {
        String result = restTemplate.getForObject("http://user-service/test/hystrix", String.class);
        logger.info("Test fallback in Hystrix, call user-service: {}", result);
        return result;
    }

    @HystrixCommand(fallbackMethod = "hystrixFallback", ignoreExceptions = {IndexOutOfBoundsException.class}, commandKey = "testHystrix")
    public String hystrixWithException(int num) {
        if (num == 1) {
            // trigger fallback
            throw new IllegalArgumentException();
        } else if (num == 2) {
            // skip fallback as it's ignored
            throw new IndexOutOfBoundsException();
        }
        String result = "valid number: " + num;
        logger.info("Test fallback with exception in Hystrix, {}", result);
        return result;
    }

    @CacheResult(cacheKeyMethod = "getCacheKey")
    @HystrixCommand(fallbackMethod = "hystrixFallback", commandKey = "testHystrix")
    public String hystrixCacheResult(int num) {
        String result = restTemplate.getForObject("http://user-service/test/ribbon", String.class);
        logger.info("Test cache result in Hystrix, call user-service, num: {}", num);
        return result;
    }

    @CacheRemove(cacheKeyMethod = "getCacheKey", commandKey = "testHystrix")
    @HystrixCommand
    public void hystrixCacheRemove(int num) {
        logger.info("Test cache remove in Hystrix, num: {}", num);
    }

    /**
     * Return Future object, otherwise they are synchronized requests and won't be merged
     */
    @HystrixCollapser(batchMethod = "hystrixMultiple", scope = com.netflix.hystrix.HystrixCollapser.Scope.GLOBAL, collapserProperties = {
            @HystrixProperty(name = "timerDelayInMilliseconds", value = "1000")})
    public Future<String> hystrixSingle(String str) {
        logger.info("hystrixSingle, str: {}", str);
        return null;
    }

    @HystrixCommand
    public List<String> hystrixMultiple(List<String> strList) {
        logger.info("trigger hystrixMultiple, list: {}", StringUtils.join(strList, ", "));
        return strList;
    }

    public String hystrixFallback(int num) {
        String result = "Fallback! num: " + num;
        logger.info(result);
        return result;
    }

    public String hystrixFallback() {
        String result = "Fallback!";
        logger.info(result);
        return result;
    }

    private String getCacheKey(int num) {
        String key = String.valueOf(num % 3);
        //logger.info("Get cache key, {} -> {}", num, key);
        return key;
    }
}
