package com.iss.info.security.system.controller;

import com.iss.info.security.system.model.Person;
import com.iss.info.security.system.model.PersonContact;
import com.iss.info.security.system.model.PersonMessage;
import com.iss.info.security.system.service.MessageService;
import com.iss.info.security.system.service.PersonContactService;
import com.iss.info.security.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;


    @Autowired
    private PersonContactService personContactService;

    @GetMapping("/getAllChats")
    public ResponseEntity<List<Person>> getAllUsers(){
       return ResponseEntity.ok(userService.getAllUser());
    }

    @GetMapping("/getAllUserChats")
    public ResponseEntity<List<PersonContact>> getAllUserChats(@RequestParam("userId") int userId){
        if(userId == 0) return ResponseEntity.badRequest().body(new ArrayList<>());
        return ResponseEntity.ok(personContactService.findAllPersonContactByPersonId(userId));
    }

    @GetMapping("/getChatMessages")
    public ResponseEntity<List<PersonMessage>> getChatMessages(@RequestParam("phoneNumber") String phoneNumber, HttpServletRequest httpServletRequest){
        if(phoneNumber == null || phoneNumber.isEmpty()) return ResponseEntity.badRequest().body(new ArrayList<>());
        return ResponseEntity.ok(messageService.getAllMessagesByPhoneNumber(httpServletRequest.getRemoteAddr(),phoneNumber));
    }


    @PostMapping("/createContact")
    public ResponseEntity<?> createContact(@RequestBody PersonContact personContact){
        personContactService.create(personContact);
        return ResponseEntity.ok("successfully");
    }
}
