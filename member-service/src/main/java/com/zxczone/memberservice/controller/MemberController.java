package com.zxczone.memberservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class MemberController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    CounterService counterService;

    @Autowired
    GaugeService gaugeService;

    @Autowired
    RestTemplate restTemplate;

    @Value("${memberservice.random.int}")
    Integer randomInt;

    @RequestMapping(value = "/members", method = RequestMethod.GET)
    public String getAllMembers() {
        String result = restTemplate.getForObject("http://user-service/users", String.class);
        logger.info("Get all users from user-service: {}", result);
        return result;
    }

    /* Can be accessed in /metrics, provided by Actuator */

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public String updateCount(){
        counterService.increment("memberservice.count");
        return "increate count";
    }

    @RequestMapping(value = "/gauge", method = RequestMethod.GET)
    public String updateGauge(){
        gaugeService.submit("memberservice.gauge", randomInt);
        return "update gauge";
    }

}
