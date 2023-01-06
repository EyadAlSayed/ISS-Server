package com.iss.info.security.system.controller;

import com.iss.info.security.system.InfoSecuritySystemApplication;
import com.iss.info.security.system.model.*;
import com.iss.info.security.system.model.req.LoginModel;
import com.iss.info.security.system.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.Key;

import static com.iss.info.security.system.helper.EncryptionConverters.hexStringToByteArray;
import static com.iss.info.security.system.helper.EncryptionTools.do_RSADecryption;

@RestController
public class AuthController {

    @Autowired
    PersonService personService;


    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> signUp(@RequestBody SignupModel model, HttpServletRequest httpServletRequest) {

        Person person = new Person();
        person.setPersonIp(new PersonIP(0, httpServletRequest.getRemoteAddr()));
        person.setName(model.getName());
        person.setPhoneNumber(model.getPhoneNumber());
        person.setPassword(model.getPassword());
        person.setPersonSessionKey(new PersonSessionKey(0, model.getSessionKey(), person));
        person.setPersonPublicKey(new PersonPublicKey(0, model.getUserPublicKey(), person));
        personService.create(person);
        return ResponseEntity.ok("successfully");
    }



    @PostMapping("/login")
    private ResponseEntity<LoginModel> login(@RequestBody LoginModel loginModel, HttpServletRequest httpServletRequest) {
        boolean isOk = personService.isRegistered(loginModel);
        if (isOk) {
            int userId = personService.updatePersonIp(loginModel.getPhoneNumber(), httpServletRequest.getRemoteAddr());
            return new ResponseEntity<>(new LoginModel(userId, loginModel.getPhoneNumber(), loginModel.getPassword()), HttpStatus.OK);
        } else return new ResponseEntity<>(new LoginModel(), HttpStatus.BAD_REQUEST);
    }
}
