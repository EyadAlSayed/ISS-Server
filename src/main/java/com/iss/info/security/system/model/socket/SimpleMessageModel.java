package com.iss.info.security.system.model.socket;

import com.google.gson.Gson;

public class SimpleMessageModel {

    private String to;
    private String from;
    private String messageBody;

    public SimpleMessageModel(String to, String from, String messageBody) {
        this.to = to;
        this.from = from;
        this.messageBody = messageBody;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public static SimpleMessageModel fromJson(String json){
        try {
            return new Gson().fromJson(json, SimpleMessageModel.class);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
