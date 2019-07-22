package com.zxczone.userservice.controller;

import com.google.common.collect.Lists;
import com.zxczone.userservice.pojo.User;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/test")
public class TestController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${server.port}")
    private int serverPort;

    /**
     * Test Ribbon
     */
    @RequestMapping(value = "/ribbon", method = RequestMethod.GET)
    public String ribbonProducer() {
        String result = "Test Ribbon, server port: " + serverPort;
        logger.info(result);
        return result;
    }

    /**
     * Test Hystrix
     */
    @RequestMapping(value = "/hystrix", method = RequestMethod.GET)
    public String hystrixProducer() throws InterruptedException {
        // sleep random seconds
        long sleepTime = RandomUtils.nextInt(3000);
        Thread.sleep(sleepTime);

        String result = "Test Hystrix, sleep: " + sleepTime + " ms";
        logger.info(result);

        return result;
    }

}
