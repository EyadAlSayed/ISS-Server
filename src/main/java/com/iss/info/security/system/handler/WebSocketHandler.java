package com.iss.info.security.system.handler;

import com.iss.info.security.system.model.Person;
import com.iss.info.security.system.model.PersonMessage;
import com.iss.info.security.system.model.PersonSessionKey;
import com.iss.info.security.system.model.socket.SocketModel;
import com.iss.info.security.system.service.PersonService;
import com.iss.info.security.system.service.SessionKeyService;
import com.iss.info.security.system.service.SocketService;
import com.iss.info.security.system.service.UserService;
import com.iss.info.security.system.socket.ClientSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import javax.crypto.SecretKey;
import java.security.PrivateKey;

import static com.iss.info.security.system.app.Constant.*;
import static com.iss.info.security.system.helper.EncryptionConverters.*;
import static com.iss.info.security.system.helper.EncryptionKeysUtils.getServerPrivateKeyFromFile;
import static com.iss.info.security.system.helper.EncryptionTools.*;

@Component
public class WebSocketHandler extends AbstractWebSocketHandler {



    @Autowired
    PersonService personService;

    @Autowired
    SessionKeyService sessionKeyService;

    @Autowired
    UserService userService;

    ClientSocket clientSocket;

    public WebSocketHandler(ClientSocket clientSocket) {
        this.clientSocket = clientSocket;
    }

    private final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        logger.info("Connection Established  With Device " + session.getRemoteAddress() + " for messaging ..");
        clientSocket.addClientSession(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        logger.info("Connection Closed With Device " + session.getRemoteAddress() + " for messages ..");
        clientSocket.removeClientSession(session);
        sessionKeyService.deleteUserSessionKey(userService.getUserByIPAddress(session.getRemoteAddress().getAddress().getHostName()).getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        logger.info("Received Message  :" + SocketModel.fromJson(message));
        clientSocket.filterAndForwardMessage(SocketModel.fromJson(message),session.getRemoteAddress().getHostName());
    }


    private void filterMessageAndSend(SocketModel socketModel) throws Exception {
        PersonMessage personMessage = PersonMessage.fromJson(socketModel.getMethodBody());
        switch (socketModel.getMethodName().toUpperCase()){
            case CHAT_SEND: {
                personMessage.setContent(decryptMessage(personMessage));
                clientSocket.sendTextMessageTo(socketService.getChatIpByPhoneNumber(personMessage.getToUser()), personMessage);
                break;
            }
            case HANDSHAKING:{
                if (sessionKeyService.getSessionKeyByUserId(userService.getUserByPhoneNumber(personMessage.getFromUser()).getId()) == null) {
                    addUserSessionKeyToDB(userService.getUserByPhoneNumber(personMessage.getFromUser()), personMessage.getContent());
                } else{
                    sessionKeyService.updateUserSessionKey(userService.getUserByPhoneNumber(personMessage.getFromUser()).getId(), personMessage.getContent());
                }
                //todo: send confirmation message to client.
                break;
            }
            default:break;
        }
    }

    // decrypts the received message using sender's session key.
    private String decryptMessage(PersonMessage personMessage) throws Exception {
        byte[] senderSessionKeyAsBytes = hexStringToByteArray(sessionKeyService.getSessionKeyByUserId(userService.getUserByPhoneNumber(personMessage.getFromUser()).getId()));
        SecretKey senderSessionKey = retrieveSymmetricSecretKey(do_RSADecryption(senderSessionKeyAsBytes, retrievePrivateKey(getServerPrivateKeyFromFile())));
        return do_AESDecryption(hexStringToByteArray(personMessage.getContent()), senderSessionKey);
    }

    private void addUserSessionKeyToDB(Person person, String encryptedSessionKey) throws Exception {
        PersonSessionKey personSessionKey = new PersonSessionKey();
        personSessionKey.setPerson(person);
        PrivateKey serverPrivateKey = retrievePrivateKey(getServerPrivateKeyFromFile());
        personSessionKey.setSessionKey(do_RSADecryption(hexStringToByteArray(encryptedSessionKey), serverPrivateKey));
        sessionKeyService.addUserSessionKey(personSessionKey);
    }
}
