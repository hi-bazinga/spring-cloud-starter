package com.zxczone.consumerservice.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * Test load balance in Ribbon
     */
    @RequestMapping(value = "/ribbon", method = RequestMethod.GET)
    public String testRibbon() {
        logger.info("Test Ribbon, call user-service");
        String result = restTemplate.getForObject("http://user-service/test/ribbon", String.class);
        logger.info("Test Ribbon, call user-service: {}", result);
        return result;
    }

    /**
     * Test circuit breaker in Hystrix
     */
    @HystrixCommand(fallbackMethod = "hystrixFallback", commandKey = "testHystrix")
    @RequestMapping(value = "/hystrix", method = RequestMethod.GET)
    public String testHystrix() {
        String result = restTemplate.getForObject("http://user-service/test/hystrix", String.class);
        logger.info("Test Hystrix, call user-service: {}", result);
        return result;
    }

    public String hystrixFallback() {
        String result = "Get result from hystrix fallback!";
        logger.info(result);
        return result;
    }

}
