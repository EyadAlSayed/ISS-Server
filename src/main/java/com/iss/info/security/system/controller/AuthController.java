package com.iss.info.security.system.controller;

import com.google.gson.JsonObject;
import com.iss.info.security.system.model.Person;
import com.iss.info.security.system.model.PersonIP;
import com.iss.info.security.system.model.req.LoginModel;
import com.iss.info.security.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthController {

    @Autowired
    UserService userService;

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> signUp(@RequestBody Person person, HttpServletRequest httpServletRequest) {
        person.setPersonIp(new PersonIP(0,httpServletRequest.getRemoteAddr()));
        userService.create(person);
        return ResponseEntity.ok("successfully");
    }

    @PostMapping("/login")
    private ResponseEntity<LoginModel> login(@RequestBody LoginModel loginModel, HttpServletRequest httpServletRequest) {
        boolean isOk = userService.isRegistered(loginModel);
        if (isOk) {
           int userId = userService.updateUserIp(loginModel.getPhoneNumber(), httpServletRequest.getRemoteAddr());

            return new ResponseEntity<>(new LoginModel(userId,loginModel.getPhoneNumber(),loginModel.getPassword()), HttpStatus.OK);
        } else return new ResponseEntity<>(new LoginModel(), HttpStatus.BAD_REQUEST);
    }
}
