package com.iss.info.security.system.service;

import com.iss.info.security.system.model.PersonIP;
import com.iss.info.security.system.repo.PersonIpRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonIpService {

    @Autowired
    PersonIpRepo personIpRepo;

    public void create(PersonIP personIP){
        personIpRepo.save(personIP);
    }
}
