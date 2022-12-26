package com.iss.info.security.system.controller;

import com.google.gson.JsonObject;
import com.iss.info.security.system.model.Person;
import com.iss.info.security.system.model.req.LoginModel;
import com.iss.info.security.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthController {

    @Autowired
    UserService userService;

    @PostMapping("/signup")
    private ResponseEntity<?> signUp(@RequestBody Person person, HttpServletRequest httpServletRequest) {
        person.getUserIp().setIp(httpServletRequest.getRemoteAddr());
        userService.create(person);
        return ResponseEntity.ok("successfully");
    }

    @PostMapping("/login")
    private ResponseEntity<JsonObject> login(@RequestBody LoginModel loginModel, HttpServletRequest httpServletRequest) {
        boolean isOk = userService.isRegistered(loginModel);
        if (isOk) {
           int userId = userService.updateUserIp(loginModel.getPhoneNumber(), httpServletRequest.getRemoteAddr());
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("userId",userId);
            return ResponseEntity.ok(jsonObject);
        } else return ResponseEntity.badRequest().body(new JsonObject());
    }
}
