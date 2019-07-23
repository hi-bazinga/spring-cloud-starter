package com.zxczone.consumerservice.controller;

import com.zxczone.consumerservice.service.HystrixService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = "/consumer")
public class ConsumerController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    HystrixService hystrixService;

    /**
     * Test load balance in Ribbon
     */
    @RequestMapping(value = "/ribbon", method = RequestMethod.GET)
    public String testRibbon() {
        String result = restTemplate.getForObject("http://user-service/test/ribbon", String.class);
        logger.info("Test load balance in Ribbon, call user-service: {}", result);
        return result;
    }

    /**
     * Test fallback in Hystrix
     */
    @RequestMapping(value = "/hystrix", method = RequestMethod.GET)
    public String testHystrix() {
        return hystrixService.hystrix();
    }

    /**
     * Test fallback with exception in Hystrix
     */
    @RequestMapping(value = "/hystrix_exception/{number}", method = RequestMethod.GET)
    public String testHystrixWithException(@PathVariable("number") int num) {
        return hystrixService.hystrixWithException(num);
    }

    /**
     * Test cache in Hystrix, cache key = num % 3
     */
    @RequestMapping(value = "/hystrix_cache/{number}", method = RequestMethod.GET)
    public String testHystrixWithCache(@PathVariable("number") int num) {
        logger.info("==== Test cache starts ====");

        logger.info("1st call, num: {}", num);
        hystrixService.hystrixCacheResult(num);
        logger.info("2nd call, num: {}", num += 1);
        hystrixService.hystrixCacheResult(num);
        logger.info("3rd call, num: {}", num += 2);
        hystrixService.hystrixCacheResult(num);
        logger.info("remove cache, num: {}", num += 3);
        hystrixService.hystrixCacheRemove(num);
        logger.info("4th call, num: {}", num);
        hystrixService.hystrixCacheResult(num);

        logger.info("==== Test cache ends ====");
        return "success";
    }

    /**
     * Test collapser in Hystrix
     */
    @RequestMapping(value = "/hystrix_collapser/{str}", method = RequestMethod.GET)
    public String testHystrixCollapser(@PathVariable("str") String str) throws InterruptedException {
        logger.info("==== Test collapser starts ====");

        hystrixService.hystrixSingle(str);
        hystrixService.hystrixSingle(str);
        hystrixService.hystrixSingle(str);

        Thread.sleep(1500);
        hystrixService.hystrixSingle(str);
        hystrixService.hystrixSingle(str);

        logger.info("==== Test collapser ends ====");
        return "success";
    }

}
