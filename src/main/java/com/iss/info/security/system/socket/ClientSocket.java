package com.iss.info.security.system.socket;

import com.iss.info.security.system.helper.DigitalSignatureTools;
import com.iss.info.security.system.helper.EncryptionKeysUtils;
import com.iss.info.security.system.model.Person;
import com.iss.info.security.system.model.PersonMessage;
import com.iss.info.security.system.model.PersonPublicKey;
import com.iss.info.security.system.model.PersonSessionKey;
import com.iss.info.security.system.model.socket.SocketModel;
import com.iss.info.security.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import static com.iss.info.security.system.app.Constant.*;
import static com.iss.info.security.system.helper.DigitalSignatureTools.verifyDigitalSignature;
import static com.iss.info.security.system.helper.EncryptionConverters.*;
import static com.iss.info.security.system.helper.EncryptionKeysUtils.getServerPrivateKeyFromFile;
import static com.iss.info.security.system.helper.EncryptionTools.*;


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
    SessionKeyService sessionKeyService;

    @Autowired
    PublicKeyService publicKeyService;

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

    public void filterAndForwardMessage(SocketModel socketModel, String userIp) throws Exception {
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
                break;
            }

//            case UPDATE_SESSION_KEY: {
//                Person fromPerson = personService.getPersonByPhoneNumber(personMessage.getFromUser());
//                if(fromPerson != null)
//                    sessionKeyService.updateUserSessionKey(fromPerson.getId(), personMessage.getContent());
//            }

            case HANDSHAKING:{
                PersonMessage personMessage = PersonMessage.fromJson(socketModel.getMethodBody());
                Person person = personService.getPersonByPhoneNumber(personMessage.getFromUser());
                if(sessionKeyService.getSessionKeyByUserId(person.getId()) == null) {
                    PersonSessionKey personSessionKey = new PersonSessionKey();
                    personSessionKey.setPerson(person);
                    personSessionKey.setSessionKey(decryptSessionKey(personMessage.getContent()));
                    sessionKeyService.addUserSessionKey(personSessionKey);
                } else {
                    sessionKeyService.updateUserSessionKey(person.getId(), personMessage.getContent());
                }
                break;
            }

            case STORING:{
                PersonMessage personMessage = PersonMessage.fromJson(socketModel.getMethodBody());
                Person person = personService.getPersonByPhoneNumber(personMessage.getFromUser());
                if(publicKeyService.getPublicKeyByUserId(person.getId()) == null) {
                    PersonPublicKey personPublicKey = new PersonPublicKey();
                    personPublicKey.setPerson(person);
                    personPublicKey.setPublicKey(personMessage.getContent());
                    publicKeyService.addUserPublicKey(personPublicKey);
                } else {
                    publicKeyService.updateUserPublicKey(person.getId(), personMessage.getContent());
                }
                break;
            }
            default:
                break;
        }
    }

    private String decryptSessionKey(String encryptedSessionKey){
        try {
            return do_RSADecryption(hexStringToByteArray(encryptedSessionKey), retrievePrivateKey(getServerPrivateKeyFromFile()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
    private void sendVerifiedMessage(SocketModel socketModel) throws Exception {
        PersonMessage personMessage = PersonMessage.fromJson(socketModel.getMethodBody());
        Person toPerson = personService.getPersonByPhoneNumber(personMessage.getToUser());
        Person fromPerson = personService.getPersonByPhoneNumber(personMessage.getFromUser());
        String receiverIp = toPerson.getPersonIp().getIp();
        String senderIp = fromPerson.getPersonIp().getIp();

        WebSocketSession receiverSession = sessions.stream().filter(it -> it.getRemoteAddress().getAddress().getHostName().equals(senderIp)).findFirst().orElse(null);
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
                    , retrieveSymmetricSecretKey(sessionKeyService.getSessionKeyByUserId(personService.getPersonByPhoneNumber(personMessage.getFromUser()).getId()))));
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
        personMessage.setContent(convertByteToHexadecimal(do_AESEncryption(decryptedMessage
                , retrieveSymmetricSecretKey(personService.getSymmetricKeyByPhoneNumber(personMessage.getToUser())))));
        socketModel.setMethodBody(personMessage.toJson());
        session.sendMessage(new TextMessage(socketModel.toJson()));
    }

    private String decryptionMessage(PersonMessage personMessage) {
        try {
            return do_AESDecryption(hexStringToByteArray(personMessage.getContent())
                    , retrieveSymmetricSecretKey(personService.getSymmetricKeyByPhoneNumber(personMessage.getFromUser())));
        } catch (Exception e) {
            return "";
        }
    }
    private void saveDecryptedSentMessage(PersonMessage personMessage) throws Exception {
        personMessage.setContent(do_AESDecryption(hexStringToByteArray(personMessage.getContent()),
                retrieveSymmetricSecretKey(sessionKeyService.getSessionKeyByUserId(personService.getPersonByPhoneNumber(personMessage.getFromUser()).getId()))));
        //personMessage.setPerson(personService.getPersonByPhoneNumber(personMessage.getFromUser()));
        messageService.saveMessage(personMessage);
    }

    private boolean verified(SocketModel socketModel) throws Exception {
        //todo: digital signature.
        PersonMessage personMessage = PersonMessage.fromJson(socketModel.getMethodBody());
        return verifyDigitalSignature(DatatypeConverter.parseHexBinary(personMessage.getContent())
        , hexStringToByteArray(socketModel.getDigitalSignature())
        , retrievePublicKey(publicKeyService.getPublicKeyByUserId(personService.getPersonByPhoneNumber(personMessage.getFromUser()).getId()))); //fixme: ...
    }
}
