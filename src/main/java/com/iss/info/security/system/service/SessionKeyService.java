package com.iss.info.security.system.service;

import com.iss.info.security.system.model.PersonSessionKey;
import com.iss.info.security.system.repo.SessionKeyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionKeyService {

    @Autowired
    SessionKeyRepo sessionKeyRepo;

    public String getSessionKeyByUserId(int userId){
        return sessionKeyRepo.getSessionKeyByUserId(userId).get().getSessionKey();
    }

    public void deleteUserSessionKey(int userId){
        sessionKeyRepo.deleteUserSessionKey(userId);
    }

    public void addUserSessionKey(PersonSessionKey personSessionKey){
        sessionKeyRepo.save(personSessionKey);
    }

    public void updateUserSessionKey(int userId, String personSessionKey){
        sessionKeyRepo.updateUserSessionKey(userId, personSessionKey);
    }
}
