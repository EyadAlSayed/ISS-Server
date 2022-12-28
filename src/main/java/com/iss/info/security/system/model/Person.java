package com.iss.info.security.system.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String phoneNumber;

    private String password;

    @OneToMany(mappedBy = "personParent")
    private Set<Person> contacts;
    @ManyToOne(fetch = FetchType.LAZY)
    private Person personParent;

    @OneToMany(mappedBy = "person")
    @JsonIgnoreProperties(value = "person", allowSetters = true)
    private List<PersonMessage> personMessages;

    @OneToOne(mappedBy = "person")
    PersonIP personIp;

    @OneToOne(mappedBy = "person")
    PersonSymmetricKey person_sym_key;

    public Person() {
    }

    public Person(int id, String name, String phoneNumber, String password) {
        this.id = id;
        this.name = name;
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



    public Set<Person> getContacts() {
        return contacts;
    }

    public void setContacts(Set<Person> contacts) {
        this.contacts = contacts;
    }

    public Person getPersonParent() {
        return personParent;
    }

    public void setPersonParent(Person personParent) {
        this.personParent = personParent;
    }

    public List<PersonMessage> getUserMessages() {
        return personMessages;
    }

    public void setUserMessages(List<PersonMessage> personMessages) {
        this.personMessages = personMessages;
    }

    public PersonIP getUserIp() {
        return personIp;
    }

    public PersonSymmetricKey getPerson_sym_key() {
        return person_sym_key;
    }

    public void setUserIp(PersonIP personIp) {
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
