package com.iss.info.security.system.service;

import com.iss.info.security.system.model.PersonSymmetricKey;
import com.iss.info.security.system.repo.PersonSymKeyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonSymmetricKeyService {

    @Autowired
    PersonSymKeyRepo personSymKeyRepo;

    public void create(PersonSymmetricKey personSymmetricKey){
        personSymKeyRepo.save(personSymmetricKey);
    }

    public String getSymmetricKeyByUserId(int userId){
        return personSymKeyRepo.getSymmetricKeyByUserId(userId).get().getSymmetricKey();
    }
}
