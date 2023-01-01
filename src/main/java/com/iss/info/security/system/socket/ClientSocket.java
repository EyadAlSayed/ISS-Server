package com.iss.info.security.system.socket;

import com.iss.info.security.system.model.PersonMessage;
import com.iss.info.security.system.service.MessageService;
import com.iss.info.security.system.service.SessionKeyService;
import com.iss.info.security.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.iss.info.security.system.helper.EncryptionConverters.convertByteToHexadecimal;
import static com.iss.info.security.system.helper.EncryptionConverters.retrieveSymmetricSecretKey;
import static com.iss.info.security.system.helper.EncryptionTools.do_AESEncryption;

@Component("clientSocket")
public class ClientSocket {

    private final List<WebSocketSession> sessions;

    @Autowired
    SessionKeyService sessionKeyService;

    @Autowired
    UserService userService;

    public ClientSocket() {
        sessions = new ArrayList<>();
    }

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
            try {
                messageService.saveMessage(personMessage);
                session.sendMessage(new TextMessage(getEncryptedMessage(personMessage)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private String getEncryptedMessage(PersonMessage personMessage) throws Exception {
        return convertByteToHexadecimal(do_AESEncryption(personMessage.getContent(), retrieveSymmetricSecretKey(sessionKeyService.getSessionKeyByUserId(userService.getUserByPhoneNumber(personMessage.getToUser()).getId()))));
    }

}
