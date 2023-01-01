package com.iss.info.security.system.model;

import javax.persistence.*;

@Entity
@Table(name = " person_sym_key")
public class PersonSymmetricKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    String symmetricKey;

    @OneToOne
    @JoinColumn(name = "person_sym_key")
    Person person;

    public PersonSymmetricKey() {
    }

    public PersonSymmetricKey(int id, String symmetricKey, Person person) {
        this.id = id;
        this.symmetricKey = symmetricKey;
        this.person = person;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSymmetricKey() {
        return symmetricKey;
    }

    public void setSymmetricKey(String symmetricKey) {
        this.symmetricKey = symmetricKey;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
