package com.iss.info.security.system.service;

import com.iss.info.security.system.model.Person;
import com.iss.info.security.system.model.req.LoginModel;
import com.iss.info.security.system.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;


    public void create(Person person) {
        userRepo.save(person);
    }


    public String getUserIpByPhoneNumber(String phoneNumber) {
       return userRepo.findByPhoneNumber(phoneNumber).get().getUserIp().getIp();
    }

    public boolean isRegistered(LoginModel loginInfo) {
//        return userRepo.exists(Example.of(new User(loginInfo.getPhoneNumber(), loginInfo.getPassword())));
        return false;
    }

    public List<Person> getAllUser(){
        return userRepo.findAll();
    }

    public Person getUserByPhoneNumber(String phoneNumber){
        return userRepo.getUserByPhoneNumber(phoneNumber).get();
    }

    public List<Person> getAllUserChats(int userId){
        return userRepo.findByPersonParentId(userId);
    }

    public int updateUserIp(String phoneNumber, String userIp) {
        Person person = getUserByPhoneNumber(phoneNumber);
        person.getUserIp().setIp(userIp);
        userRepo.save(person);
        return person.getId();
    }
}
