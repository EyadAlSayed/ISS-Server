package com.iss.info.security.system.controller;

import com.iss.info.security.system.model.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @PostMapping("/signup")
    private Boolean signUp(@RequestBody User user){
        return false;
    }


    @PostMapping("/login")
    private Boolean login(@RequestBody User user){
        return false;
    }
}
