package com.csfw.jexx.digest.controller;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @USER wh
 * @DATE 2020/6/10
 * @Description
 */

@RestController
@RequestMapping("/")
public class LoginController {
    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String login(@RequestHeader Map<String, Object> headers){
        return "Login success...";
    }
}
