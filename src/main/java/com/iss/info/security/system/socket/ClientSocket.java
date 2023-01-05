package com.iss.info.security.system.socket;

import com.iss.info.security.system.helper.SymmetricEncryptionTools;
import com.iss.info.security.system.model.Person;
import com.iss.info.security.system.model.PersonMessage;
import com.iss.info.security.system.model.socket.SocketModel;
import com.iss.info.security.system.service.MessageService;
import com.iss.info.security.system.service.PersonSymmetricKeyService;
import com.iss.info.security.system.service.SocketService;
import com.iss.info.security.system.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static com.iss.info.security.system.app.Constant.*;
import static com.iss.info.security.system.helper.SymmetricEncryptionTools.*;

@Component("clientSocket")
public class ClientSocket {
    @Autowired
    SocketService socketService;
    private final List<WebSocketSession> sessions;

    public ClientSocket() {
        sessions = new ArrayList<>();
    }

    @Autowired
    PersonService personService;

    @Autowired
    PersonSymmetricKeyService personSymmetricKeyService;

    @Autowired
    MessageService messageService;

    public List<WebSocketSession> getSessions() {
        return sessions;
    }

    public void addClientSession(WebSocketSession session) {
        sessions.add(session);
    }

    public void removeClientSession(WebSocketSession session) {
        sessions.remove(session);
    }

    public void filterAndForwardMessage(SocketModel socketModel, String userIp) {
        switch (socketModel.getMethodName().toUpperCase()) {
            case CHAT_SEND:
            case CHAT_RECEIVED: {
                sendAndReceiveFilter(socketModel);
                break;
            }
            case CHAT_SEND_E:
            case CHAT_RECEIVED_E: {
                sendVerifiedMessage(socketModel);
                break;
            }
            case REG_IP: {
                String phoneNumber = socketModel.getMethodBody();
//                socketService.updatePersonIp(phoneNumber, userIp);
            }
            default:
                break;
        }
    }

    public void sendAndReceiveFilter(SocketModel socketModel) {
        PersonMessage personMessage = PersonMessage.fromJson(socketModel.getMethodBody());
        Person person = personService.getPersonByPhoneNumber(personMessage.getFromUser());
        personMessage.setPerson(person);
        String receiverIp = person.getPersonIp().getIp();
        try {
            sendTextMessageTo(receiverIp, socketModel);
            socketService.saveMessage(personMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTextMessageTo(String receiverIp, SocketModel socketModel) throws Exception {
        WebSocketSession session = sessions.stream().filter(it -> it.getRemoteAddress().getAddress().getHostName().equals(receiverIp)).findFirst().orElse(null);
        if (session != null) {
            session.sendMessage(new TextMessage(socketModel.toJson()));
        }
    }











    private void sendVerifiedMessage(SocketModel socketModel) {
        PersonMessage personMessage = PersonMessage.fromJson(socketModel.getMethodBody());
        Person toPerson = personService.getPersonByPhoneNumber(personMessage.getToUser());
        Person fromPerson = personService.getPersonByPhoneNumber(personMessage.getFromUser());
        String receiverIp = toPerson.getPersonIp().getIp();
        String senderIp = fromPerson.getPersonIp().getIp();

        System.out.println("Message : " + personMessage.getContent());

        WebSocketSession receiverSession = sessions.stream().filter(it -> it.getRemoteAddress().getAddress().getHostName().equals(receiverIp)).findFirst().orElse(null);
        try {
            if (verified(socketModel)) sendTextEncryptedMessage(socketModel, receiverSession);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        try {
            WebSocketSession senderSession = sessions.stream().filter(it -> it.getRemoteAddress().getAddress().getHostName().equals(senderIp)).findFirst().orElse(null);
            senderSession.sendMessage(new TextMessage(new SocketModel(CHAT_RECEIVED_E,encryptMessage(personMessage, "message sent")).toJson()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String encryptMessage(PersonMessage personMessage, String message){
        try {
            return convertByteToHexadecimal(do_AESEncryption(message
            , retrieveSecretKey(personSymmetricKeyService.getSymmetricKeyByUserId(personService.getPersonByPhoneNumber(personMessage.getFromUser()).getId()))));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendTextEncryptedMessage(SocketModel socketModel, WebSocketSession session) {
        try {
            PersonMessage personMessage = PersonMessage.fromJson(socketModel.getMethodBody());
            saveDecryptedSentMessage(personMessage);
            sendEncryptedMessage(session, socketModel);
        } catch (Exception ignore) {

        }
    }

    private void sendEncryptedMessage(WebSocketSession session, SocketModel socketModel) throws Exception {
        PersonMessage personMessage = PersonMessage.fromJson(socketModel.getMethodBody());
        String decryptedMessage = decryptionMessage(personMessage);
        personMessage.setContent(SymmetricEncryptionTools.convertByteToHexadecimal(do_AESEncryption(decryptedMessage
                , SymmetricEncryptionTools.retrieveSecretKey(personService.getSymmetricKeyByPhoneNumber(personMessage.getToUser())))));
        socketModel.setMethodBody(personMessage.toJson());
        session.sendMessage(new TextMessage(socketModel.toJson()));
    }

    private String decryptionMessage(PersonMessage personMessage) {
        try {
            return SymmetricEncryptionTools.do_AESDecryption(SymmetricEncryptionTools.hexStringToByteArray(personMessage.getContent())
                    , SymmetricEncryptionTools.retrieveSecretKey(personService.getSymmetricKeyByPhoneNumber(personMessage.getFromUser())));
        } catch (Exception e) {
            return "";
        }
    }
    private void saveDecryptedSentMessage(PersonMessage personMessage) throws Exception {
        personMessage.setContent(do_AESDecryption(hexStringToByteArray(personMessage.getContent()),
                retrieveSecretKey(personSymmetricKeyService.getSymmetricKeyByUserId(personService.getPersonByPhoneNumber(personMessage.getFromUser()).getId()))));
        //personMessage.setPerson(personService.getPersonByPhoneNumber(personMessage.getFromUser()));
        messageService.saveMessage(personMessage);
    }

    private boolean verified(SocketModel socketModel) throws NoSuchAlgorithmException, InvalidKeyException {
        PersonMessage personMessage = PersonMessage.fromJson(socketModel.getMethodBody());
        return socketModel.getMac()
                .equals(SymmetricEncryptionTools.getMac(personService.getSymmetricKeyByPhoneNumber(personMessage.getFromUser())
                        , personMessage.getContent()));
    }
}
