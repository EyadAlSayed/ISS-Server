package com.iss.info.security.system.service;

import com.iss.info.security.system.model.PersonPublicKey;
import com.iss.info.security.system.model.PersonSessionKey;
import com.iss.info.security.system.repo.PublicKeyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublicKeyService {

    @Autowired
    PublicKeyRepo publicKeyRepo;

    public String getPublicKeyByUserId(int userId){
        System.out.println("PublicKeyService: getPublicKeyByUserId -> userId: " + userId);
        System.out.println("PublicKeyService: getPublicKeyByUserId -> publicKey: " + publicKeyRepo.getPublicKeyByUserId(userId).get().getPublicKey());
        return publicKeyRepo.getPublicKeyByUserId(userId).get().getPublicKey();
    }

    public void deleteUserPublicKey(int userId){
        publicKeyRepo.deleteUserPublicKey(userId);
    }

    public void addUserPublicKey(PersonPublicKey personPublicKey){
        System.out.println("PublicKeyService: addUserPublicKey -> personPublicKey" + personPublicKey);
        publicKeyRepo.save(personPublicKey);
    }

    public void updateUserPublicKey(int userId, String personPublicKey){
        System.out.println("PublicKeyService: updateUserPublicKey -> personPublicKey" + personPublicKey);
        publicKeyRepo.updateUserPublicKey(userId, personPublicKey);
    }
}
