package com.iss.info.security.system.service;

import com.iss.info.security.system.InfoSecuritySystemApplication;
import com.iss.info.security.system.helper.EncryptionConverters;
import com.iss.info.security.system.helper.EncryptionTools;
import com.iss.info.security.system.model.Person;
import com.iss.info.security.system.model.PersonIP;
import com.iss.info.security.system.model.PersonPublicKey;
import com.iss.info.security.system.model.PersonSessionKey;
import com.iss.info.security.system.model.req.LoginModel;
import com.iss.info.security.system.repo.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

import static com.iss.info.security.system.helper.EncryptionConverters.*;
import static com.iss.info.security.system.helper.EncryptionTools.do_RSADecryption;
import static com.iss.info.security.system.helper.EncryptionTools.do_RSAEncryption;

@Service
public class PersonService {

    @Autowired
    private PersonRepo personRepo;

    @Autowired
    private PersonIpService personIpService;

    @Autowired
    private SessionKeyService sessionKeyService;

    @Autowired
    private PublicKeyService publicKeyService;


    public void create(Person person) {
        PersonIP pIp = person.getPersonIp();
        //PersonSymmetricKey personSymmetricKey = person.getPersonSymKey();
        PersonSessionKey personSessionKey = person.getPersonSessionKey();
        personSessionKey.setSessionKey(decryptSessionKey(person.getPersonSessionKey().getSessionKey()));
        PersonPublicKey personPublicKey = person.getPersonPublicKey();
        person.setPersonIp(null);
        person.setPersonSessionKey(null);
        person.setPersonPublicKey(null);
        Person p = personRepo.save(person);
        pIp.setPerson(p);
        //personSymmetricKey.setPerson(p);
        personSessionKey.setPerson(p);
        personPublicKey.setPerson(p);
        personIpService.create(pIp);
        sessionKeyService.addUserSessionKey(personSessionKey);
        publicKeyService.addUserPublicKey(personPublicKey);
    }

    private String decryptSessionKey(String sessionKey){
        try {
            PrivateKey privateKey = retrievePrivateKey(InfoSecuritySystemApplication.serverPrivateKey);
            System.out.println("PersonService -> decryptSession -> privateKey: " + InfoSecuritySystemApplication.serverPrivateKey);
            return do_RSADecryption(hexStringToByteArray(sessionKey), privateKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public String getPersonIpByPhoneNumber(String phoneNumber) {
        return personRepo.findPersonByPhoneNumber(phoneNumber).get().getPersonIp().getIp();
    }

    public String getPhoneNumberByUserIp(String ip) {
        return personRepo.findPhoneNumberByPersonIp(ip);
    }

    public boolean isRegistered(LoginModel loginInfo) {
        return personRepo.findByPhoneNumberAndPassword(loginInfo.getPhoneNumber(), loginInfo.getPassword()).isPresent();
    }
    public boolean isPhoneNumberExist(String phoneNumber){
        return personRepo.existsByPhoneNumber(phoneNumber);
    }

    public List<Person> getAllUser() {
        return personRepo.findAll();
    }

    public Person getPersonByPhoneNumber(String phoneNumber) {
        try {
            return personRepo.findPersonByPhoneNumber(phoneNumber).get();
        } catch (Exception e) {
            return new Person();
        }
    }

    public Person getPersonById(int userId) {
        return personRepo.findById(userId).get();
    }

    public String getSymmetricKeyByPhoneNumber(String phoneNumber) {
        return personRepo.findPersonByPhoneNumber(phoneNumber).get().getPersonSessionKey().getSessionKey();
    }


    public int updatePersonIp(String phoneNumber, String userIp) {
        Person person = getPersonByPhoneNumber(phoneNumber);
        person.getPersonIp().setIp(userIp);
        personRepo.save(person);
        return person.getId();
    }
}
