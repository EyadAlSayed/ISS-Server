package com.iss.info.security.system.controller;

import com.google.gson.JsonObject;
import com.iss.info.security.system.model.*;
import com.iss.info.security.system.model.req.LoginModel;
import com.iss.info.security.system.service.PersonService;
import com.iss.info.security.system.service.PublicKeyService;
import com.iss.info.security.system.service.SessionKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthController {

    @Autowired
    PersonService personService;

//    @Autowired
//    SessionKeyService sessionKeyService;
//
//    @Autowired
//    PublicKeyService publicKeyService;

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> signUp(@RequestBody SignupModel model, HttpServletRequest httpServletRequest) {
        Person person = new Person();
//        person.setPersonIp(new PersonIP(0,httpServletRequest.getRemoteAddr()));
        person.setName(model.getName());
        person.setPhoneNumber(model.getPhoneNumber());
        person.setPassword(model.getPassword());
        person.setPersonSessionKey(new PersonSessionKey(0, model.getSessionKey(), person));
        person.setPersonPublicKey(new PersonPublicKey(0, model.getUserPublicKey(), person));
        personService.create(person);
//        PersonSessionKey personSessionKey = new PersonSessionKey();
//        personSessionKey.setPerson(person);
//        personSessionKey.setSessionKey(String.valueOf(model.getSessionKey()));
//        sessionKeyService.addUserSessionKey(personSessionKey);
//        PersonPublicKey personPublicKey = new PersonPublicKey();
//        personPublicKey.setPerson(person);
//        personPublicKey.setPublicKey(String.valueOf(model.getUserPublicKey()));
//        publicKeyService.addUserPublicKey(personPublicKey);
        return ResponseEntity.ok("successfully");
    }

    private void handshake(Person person){
//        person.setPersonSessionKey(new PersonSessionKey(person.getS));
//        PersonSessionKey personSessionKey = new PersonSessionKey();
//        personSessionKey.setPerson(person);
//        personSessionKey.setSessionKey(decryptSessionKey(personMessage.getContent()));
//        sessionKeyService.addUserSessionKey(personSessionKey);
    }

//    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
//    private ResponseEntity<?> signUp(@RequestParam("key") String key,@RequestBody Person person, HttpServletRequest httpServletRequest) {
//        person.setPersonIp(new PersonIP(person.getId(),httpServletRequest.getRemoteAddr()));
//        //todo: to check - person.getId().
//        person.setPersonSessionKey(new PersonSessionKey(person.getId(),key,person));
//        personService.create(person);
//        return ResponseEntity.ok("successfully");
//    }

    @PostMapping("/login")
    private ResponseEntity<LoginModel> login(@RequestBody LoginModel loginModel, HttpServletRequest httpServletRequest) {
        boolean isOk = personService.isRegistered(loginModel);
        if (isOk) {
           int userId = personService.updatePersonIp(loginModel.getPhoneNumber(), httpServletRequest.getRemoteAddr());

            return new ResponseEntity<>(new LoginModel(userId,loginModel.getPhoneNumber(),loginModel.getPassword()), HttpStatus.OK);
        } else return new ResponseEntity<>(new LoginModel(), HttpStatus.BAD_REQUEST);
    }
}
