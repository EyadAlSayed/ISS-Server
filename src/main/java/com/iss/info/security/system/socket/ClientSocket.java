package com.iss.info.security.system.socket;

import com.iss.info.security.system.helper.SymmetricEncryptionTools;
import com.iss.info.security.system.model.PersonMessage;
import com.iss.info.security.system.service.MessageService;
import com.iss.info.security.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component("clientSocket")
public class ClientSocket {

    private final List<WebSocketSession> sessions;

    public ClientSocket() {
        sessions = new ArrayList<>();
    }

    @Autowired
    UserService userService;

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

    public void sendTextMessageTo(String receiverIp, PersonMessage personMessage) throws Exception {
        WebSocketSession session = sessions.stream().filter(it -> it.getRemoteAddress().getAddress().getHostName().equals(receiverIp)).findFirst().orElse(null);
        if (session != null) {
            saveDecryptedSentMessage(personMessage);
            sendEncryptedMessage(session, personMessage);
        }
    }

    private void sendEncryptedMessage(WebSocketSession session, PersonMessage personMessage) throws Exception {
        personMessage.setContent(SymmetricEncryptionTools.convertByteToHexadecimal(SymmetricEncryptionTools.do_AESEncryption(personMessage.getContent()
                , SymmetricEncryptionTools.retrieveSecretKey(userService.getSymmetricKeyByPhoneNumber(personMessage.getToUser())))));
        session.sendMessage(new TextMessage(personMessage.getContent()));
    }

    private void saveDecryptedSentMessage(PersonMessage personMessage) throws Exception {
        personMessage.setContent(SymmetricEncryptionTools.do_AESDecryption(SymmetricEncryptionTools.hexStringToByteArray(personMessage.getContent())
                , SymmetricEncryptionTools.retrieveSecretKey(userService.getSymmetricKeyByPhoneNumber(personMessage.getFromUser()))));
        messageService.saveMessage(personMessage);
    }

}
