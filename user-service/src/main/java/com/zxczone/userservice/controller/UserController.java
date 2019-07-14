package com.zxczone.userservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Value("${server.port}")
    private int serverPort;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String getAllUsers() {
        String result = "all users, port: " + serverPort;
        logger.info(result);
        return result;
    }
}
