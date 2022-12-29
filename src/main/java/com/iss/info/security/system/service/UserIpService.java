package com.iss.info.security.system.service;

import com.iss.info.security.system.model.PersonIP;
import com.iss.info.security.system.repo.UserIpRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserIpService {

    @Autowired
    UserIpRepo userIpRepo;

    public void create(PersonIP personIP){
        userIpRepo.save(personIP);
    }
}
