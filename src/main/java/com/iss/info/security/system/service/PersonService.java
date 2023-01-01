package com.iss.info.security.system.service;

import com.iss.info.security.system.model.Person;
import com.iss.info.security.system.model.PersonIP;
import com.iss.info.security.system.model.PersonSymmetricKey;
import com.iss.info.security.system.model.req.LoginModel;
import com.iss.info.security.system.repo.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    @Autowired
    private PersonRepo personRepo;

    @Autowired
    private PersonIpService personIpService;



    public void create(Person person) {
        PersonIP pIp = person.getPersonIp();
        PersonSymmetricKey personSymmetricKey = person.getPersonSymKey();
        person.setPersonIp(null);
        Person p = personRepo.save(person);
        pIp.setPerson(p);
        personSymmetricKey.setPerson(p);
        personIpService.create(pIp);
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
        return personRepo.findPersonByPhoneNumber(phoneNumber).get().getPersonSymKey().getSymmetricKey();
    }


    public int updatePersonIp(String phoneNumber, String userIp) {
        Person person = getPersonByPhoneNumber(phoneNumber);
        person.getPersonIp().setIp(userIp);
        personRepo.save(person);
        return person.getId();
    }
}
