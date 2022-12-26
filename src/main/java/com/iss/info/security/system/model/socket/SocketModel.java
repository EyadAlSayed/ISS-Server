package com.iss.info.security.system.model.socket;

import com.google.gson.Gson;
import org.springframework.web.socket.TextMessage;

import java.io.Serializable;

public class SocketModel implements Serializable {

    private String methodName;
    private String methodBody;

    public String getMethodName() {
        return methodName;
    }

    public String getMethodBody() {
        return methodBody;
    }

    public String toJson(){
        return new Gson().toJson(this);
    }

    public static SocketModel fromJson(TextMessage textMessage){
        return new Gson().fromJson(textMessage.getPayload(),SocketModel.class);
    }

    @Override
    public String toString() {
        return "SocketModel{" +
                "methodName='" + methodName + '\'' +
                ", methodBody='" + methodBody + '\'' +
                '}';
    }
}
