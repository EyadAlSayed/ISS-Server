package com.iss.info.security.system.controller;

import com.iss.info.security.system.model.Person;
import com.iss.info.security.system.model.PersonMessage;
import com.iss.info.security.system.service.MessageService;
import com.iss.info.security.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @GetMapping("/getAllChats")
    public ResponseEntity<List<Person>> getAllUsers(){
       return ResponseEntity.ok(userService.getAllUser());
    }

    @GetMapping("/getAllUserChats")
    public ResponseEntity<List<Person>> getAllUserChats(@RequestParam("userId") int userId){
        if(userId == 0) return ResponseEntity.badRequest().body(new ArrayList<>());
        return ResponseEntity.ok(userService.getAllUser());
    }

    @GetMapping("/getChatMessages")
    public ResponseEntity<List<PersonMessage>> getChatMessages(@RequestParam("phoneNumber") String phoneNumber, HttpServletRequest httpServletRequest){
        if(phoneNumber == null || phoneNumber.isEmpty()) return ResponseEntity.badRequest().body(new ArrayList<>());
        return ResponseEntity.ok(messageService.getAllMessagesByPhoneNumber(phoneNumber));
    }
}
