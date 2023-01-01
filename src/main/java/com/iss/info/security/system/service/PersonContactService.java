package com.iss.info.security.system.service;

import com.iss.info.security.system.model.PersonContact;
import com.iss.info.security.system.repo.PersonContactRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonContactService {

    @Autowired
    PersonContactRepo personContactRepo;


    public void create(PersonContact personContact){
        personContactRepo.save(personContact);
    }
    public List<PersonContact> findAllPersonContactByPersonId(int personId){
        return  personContactRepo.findAllPersonContactByPersonId(personId);
    }
}
