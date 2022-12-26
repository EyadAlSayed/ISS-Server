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
    private UserService userService;

    public void saveMessage(PersonMessage personMessage) {
        Person person = userService.getUserByPhoneNumber(personMessage.getFromUser());
//        personMessage.setUser(person);
        messageRepo.save(personMessage);
    }

    public List<PersonMessage> getAllMessagesByPhoneNumber(String phoneNumber){
        return messageRepo.findByToUser(phoneNumber);
    }


}
