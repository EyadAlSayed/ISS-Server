package com.iss.info.security.system.socket;

import com.iss.info.security.system.model.PersonMessage;
import com.iss.info.security.system.service.MessageService;
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

    public void sendTextMessageTo(String receiverIp, PersonMessage personMessage) {
        WebSocketSession session = sessions.stream().filter(it -> it.getRemoteAddress().getAddress().getHostName().equals(receiverIp)).findFirst().orElse(null);
        if (session != null) {
            try {
                messageService.saveMessage(personMessage);
                session.sendMessage(new TextMessage(personMessage.getContent()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
