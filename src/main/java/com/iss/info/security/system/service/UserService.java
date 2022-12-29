package com.iss.info.security.system.service;

import com.iss.info.security.system.model.Person;
import com.iss.info.security.system.model.PersonIP;
import com.iss.info.security.system.model.req.LoginModel;
import com.iss.info.security.system.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserIpService userIpService;

    public void create(Person person) {
        PersonIP pIp = person.getPersonIp();
        person.setPersonIp(null);
        Person p = userRepo.save(person);
        pIp.setPerson(p);
        userIpService.create(pIp);
    }


    public String getUserIpByPhoneNumber(String phoneNumber) {
        return userRepo.findByPhoneNumber(phoneNumber).get().getPersonIp().getIp();
    }

    public String getPhoneNumberByUserIp(String ip){
        return userRepo.findPhoneNumberByUserIp(ip);
    }
    public boolean isRegistered(LoginModel loginInfo) {
        return userRepo.findByPhoneNumberAndPassword(loginInfo.getPhoneNumber(),loginInfo.getPassword()).isPresent();
    }

    public List<Person> getAllUser() {
        return userRepo.findAll();
    }

    public Person getUserByPhoneNumber(String phoneNumber) {
        return userRepo.getUserByPhoneNumber(phoneNumber).get();
    }

    public Person getUserById(int userId){
        return userRepo.getById(userId).get();
    }

    public String getSymmetricKeyByPhoneNumber(String phoneNumber){
        return userRepo.findByPhoneNumber(phoneNumber).get().getPerson_sym_key().getSymmetricKey();
    }

    public List<Person> getAllUserChats(int userId){
        return userRepo.findByPersonParentId(userId);
    }

    public int updateUserIp(String phoneNumber, String userIp) {
        Person person = getUserByPhoneNumber(phoneNumber);
        person.getPersonIp().setIp(userIp);
        userRepo.save(person);
        return person.getId();
    }
}
