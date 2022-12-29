package com.iss.info.security.system.handler;

import com.iss.info.security.system.helper.SymmetricEncryptionTools;
import com.iss.info.security.system.model.PersonMessage;
import com.iss.info.security.system.model.socket.SocketModel;
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

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static com.iss.info.security.system.app.Constant.CHAT_SEND;
import static com.iss.info.security.system.app.Constant.*;

@Component
public class WebSocketHandler extends AbstractWebSocketHandler {



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
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        logger.info("Received Message  :" + SocketModel.fromJson(message));
        if(verified(SocketModel.fromJson(message)))
            filterMessageAndSend(SocketModel.fromJson(message));
        clientSocket.filterAndForwardMessage(SocketModel.fromJson(message),session.getRemoteAddress().getHostName());
    }


    private void filterMessageAndSend(SocketModel socketModel) throws Exception {
        switch (socketModel.getMethodName().toUpperCase()){
           case  CHAT_SEND: {
               PersonMessage personMessage = PersonMessage.fromJson(socketModel.getMethodBody());
               clientSocket.sendTextMessageTo(socketService.getChatIpByPhoneNumber(personMessage.getToUser()), personMessage);
               break;
           }
            default:break;
        }
    }

    private boolean verified(SocketModel socketModel) throws NoSuchAlgorithmException, InvalidKeyException {
        PersonMessage personMessage = PersonMessage.fromJson(socketModel.getMethodBody());
        return socketModel.getMac()
                .equals(SymmetricEncryptionTools.getMac(userService.getSymmetricKeyByPhoneNumber(personMessage.getFromUser())
                , personMessage.getContent()));
    }

}
