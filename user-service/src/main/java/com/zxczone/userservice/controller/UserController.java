package com.zxczone.userservice.controller;

import com.google.common.collect.Lists;
import com.zxczone.userservice.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<User> getAllUsers() {
        logger.info("Get all users");
        //...
        return Lists.newArrayList();
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public User createUser(@RequestBody User user) {
        logger.info("Create user {}", user.getId());
        //...
        return user;
    }

    @RequestMapping(value = "/{user_id}", method = RequestMethod.PUT)
    public User updateUser(@PathVariable("user_id") String userId, @RequestBody User user) {
        logger.info("Update user {}", userId);
        //...
        return user;
    }

    @RequestMapping(value = "/{user_id}", method = RequestMethod.DELETE)
    public String deleteUser(@PathVariable("user_id") String userId) {
        logger.info("Delete user {}", userId);
        //...
        return userId;
    }

}
