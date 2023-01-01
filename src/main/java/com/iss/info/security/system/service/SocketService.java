package com.iss.info.security.system.service;

import com.iss.info.security.system.model.PersonMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SocketService {
    @Autowired
    PersonService personService;

    @Autowired MessageService messageService;

    public String getChatIpByPhoneNumber(String phoneNumber){
       return personService.getPersonIpByPhoneNumber(phoneNumber);
    }

    public void updatePersonIp(String phoneNumber,String ip){
     personService.updatePersonIp(phoneNumber, ip);
    }

    public void saveMessage(PersonMessage personMessage){
        messageService.saveMessage(personMessage);
    }


}
