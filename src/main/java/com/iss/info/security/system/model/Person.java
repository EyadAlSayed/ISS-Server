package com.iss.info.security.system.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "person")
public class Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String phoneNumber;

    private String password;

    @OneToMany(mappedBy = "person")
    @JsonIgnoreProperties(value = "person", allowSetters = true)
    @JsonManagedReference
    private Set<PersonContact> personContacts;

    @OneToMany(mappedBy = "person")
    @JsonIgnoreProperties(value = "person", allowSetters = true)
    @JsonManagedReference
    private List<PersonMessage> personMessages;

    @OneToOne(cascade = CascadeType.ALL,mappedBy = "person")
    @JsonIgnoreProperties(value = "person")
            @JsonManagedReference
    PersonIP personIp;

    public Person() {
    }

    public Person(int id, String name, String phoneNumber, String password) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public Person(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<PersonMessage> getUserMessages() {
        return personMessages;
    }

    public void setUserMessages(List<PersonMessage> personMessages) {
        this.personMessages = personMessages;
    }

    public PersonIP getPersonIp() {
        return personIp;
    }

    public void setPersonIp(PersonIP personIp) {
        this.personIp = personIp;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
