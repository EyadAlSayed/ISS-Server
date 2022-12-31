package com.iss.info.security.system.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.Gson;

import javax.persistence.*;

@Entity
@Table(name = "person_message")
public class PersonMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String content;

    private String fromUser;

    private String toUser;

    private String sender;

    private String mac;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false, referencedColumnName = "id")
    @JsonIgnoreProperties(value = "personMessages", allowSetters = true)
    @JsonBackReference
    private Person person;

    public PersonMessage() {
    }

    public PersonMessage(int id, String content, String fromUser, String toUser, String sender) {
        this.id = id;
        this.content = content;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.sender = sender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public static PersonMessage fromJson(String json) {
        try {
            return new Gson().fromJson(json, PersonMessage.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String toJson() {
        try {
            return new Gson().toJson(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
