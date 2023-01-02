package com.iss.info.security.system.model;

import javax.persistence.*;

@Entity
@Table(name = "public_keys")
public class PersonPublicKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String publicKey;

    @OneToOne
    @JoinColumn(name = "person_id")
    Person person;

    public PersonPublicKey(int id, String userPublicKey, Person person) {
        this.id = id;
        this.person = person;
        this.publicKey = userPublicKey;
    }

    public PersonPublicKey(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
