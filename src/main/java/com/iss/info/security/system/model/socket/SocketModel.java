package com.iss.info.security.system.model.socket;

import com.google.gson.Gson;
import org.springframework.web.socket.TextMessage;

import java.io.Serializable;

public class SocketModel implements Serializable {

    private String methodName;
    private String methodBody;
    private String mac;

    public SocketModel(String methodName, String methodBody) {
        this.methodName = methodName;
        this.methodBody = methodBody;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setMethodBody(String methodBody) {
        this.methodBody = methodBody;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getMethodBody() {
        return methodBody;
    }

    public String getMac(){
        return mac;
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
                ", mac='" + mac + '\'' +
                '}';
    }
}
