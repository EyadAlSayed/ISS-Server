package com.iss.info.security.system.controller;

import com.google.gson.JsonObject;
import com.iss.info.security.system.model.Person;
import com.iss.info.security.system.model.PersonContact;
import com.iss.info.security.system.model.PersonMessage;
import com.iss.info.security.system.service.MessageService;
import com.iss.info.security.system.service.PersonContactService;
import com.iss.info.security.system.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private PersonService personService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private PersonContactService personContactService;


    @GetMapping("/test")
    public ResponseEntity<?> getTest() {
        return ResponseEntity.ok(personService.getPersonByPhoneNumber("0991423014"));
    }

    @GetMapping("/getUserByPhoneNumber")
    public ResponseEntity<?> getUserByPhoneNumber(@RequestParam("phoneNumber") String phoneNumber) {
        return ResponseEntity.ok(personService.getPersonByPhoneNumber(phoneNumber));
    }

    @GetMapping("/getAllChats")
    public ResponseEntity<List<Person>> getAllUsers() {
        return ResponseEntity.ok(personService.getAllUser());
    }

    @GetMapping("/getAllUserChats")
    public ResponseEntity<List<PersonContact>> getAllUserChats(@RequestParam("userId") int userId) {
        if (userId == 0) return ResponseEntity.badRequest().body(new ArrayList<>());
        return ResponseEntity.ok(personContactService.findAllPersonContactByPersonId(userId));
    }

    @GetMapping("/getChatMessages")
    public ResponseEntity<List<PersonMessage>> getChatMessages(@RequestParam("phoneNumber") String phoneNumber, HttpServletRequest httpServletRequest) {
        if (phoneNumber == null || phoneNumber.isEmpty()) return ResponseEntity.badRequest().body(new ArrayList<>());
        return ResponseEntity.ok(messageService.getAllMessagesByPhoneNumber(httpServletRequest.getRemoteAddr(), phoneNumber));
    }


    @PostMapping("/createContact")
    public ResponseEntity<?> createContact(@RequestParam("userId") int userId, @RequestBody PersonContact personContact) {

        if (userId == 0) return ResponseEntity.badRequest().body("bad Request");

        if (!personService.isPhoneNumberExist(personContact.getPhoneNumber())) return ResponseEntity.notFound().build();

        Person person = personService.getPersonById(userId);
        personContact.setPerson(person);
        personContactService.create(personContact);
        return ResponseEntity.ok("successfully");
    }
}
