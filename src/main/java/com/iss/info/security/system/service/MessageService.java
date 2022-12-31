package com.iss.info.security.system.service;

import com.iss.info.security.system.model.Person;
import com.iss.info.security.system.model.PersonMessage;
import com.iss.info.security.system.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class MessageService {

    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private PersonService personService;

    public void saveMessage(PersonMessage personMessage) {
        Person person = personService.getPersonByPhoneNumber(personMessage.getFromUser());
        personMessage.setPerson(person);
        messageRepo.save(personMessage);
    }

    public List<PersonMessage> getAllMessagesByPhoneNumber(String ip, String phoneNumber){
        String senderPhoneNumber = personService.getPhoneNumberByUserIp(ip);
        return messageRepo.findByFromUserAndToUser(senderPhoneNumber,phoneNumber);
    }

    public List<PersonMessage> findByFromUserAndToUser(String one,String two){
        return messageRepo.findByFromUserAndToUser(one,two);
    }


}
