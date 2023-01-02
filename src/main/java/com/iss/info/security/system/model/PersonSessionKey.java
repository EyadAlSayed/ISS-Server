package com.iss.info.security.system.model;

import javax.persistence.*;

@Entity
@Table(name = "session_keys")
public class PersonSessionKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String sessionKey;

    @OneToOne
    @JoinColumn(name = "person_id")
    Person person;

    public PersonSessionKey(int id, String key, Person person) {
        this.id = id;
        this. sessionKey = key;
        this.person = person;
    }

    public PersonSessionKey(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
