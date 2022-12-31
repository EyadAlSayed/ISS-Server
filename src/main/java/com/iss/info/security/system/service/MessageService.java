package com.iss.info.security.system.service;

import com.iss.info.security.system.helper.SymmetricEncryptionTools;
import com.iss.info.security.system.model.Person;
import com.iss.info.security.system.model.PersonMessage;
import com.iss.info.security.system.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.iss.info.security.system.helper.SymmetricEncryptionTools.*;

@Service

public class MessageService {

    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private PersonService personService;

    @Autowired
    PersonSymmetricKeyService personSymmetricKeyService;

    public void saveMessage(PersonMessage personMessage) {
        Person person = personService.getPersonByPhoneNumber(personMessage.getFromUser());
        personMessage.setPerson(person);
        messageRepo.save(personMessage);
    }

    public List<PersonMessage> getAllMessagesByPhoneNumber(String ip, String phoneNumber){
        String senderPhoneNumber = personService.getPhoneNumberByUserIp(ip);
        //messages are decrypted here.
        List<PersonMessage> encryptedMessages = messageRepo.findByFromUserAndToUser(senderPhoneNumber, phoneNumber);
        for (PersonMessage personMessage : encryptedMessages) {
            try {
                //set encrypted messages.
                personMessage.setContent(convertByteToHexadecimal(do_AESEncryption(personMessage.getContent()
                , retrieveSecretKey(personSymmetricKeyService.getSymmetricKeyByUserId(personService.getPersonByPhoneNumber(senderPhoneNumber).getId())))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return encryptedMessages;
    }


}
