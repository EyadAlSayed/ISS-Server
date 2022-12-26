package com.iss.info.security.system.model;

import javax.persistence.*;

@Entity
@Table(name = "person_ip")
public class PersonIP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String ip;

    @OneToOne
    @JoinColumn(name = "person_id", nullable = true)
    Person person;


    public PersonIP() {
    }

    public PersonIP(int id, String ip) {
        this.id = id;
        this.ip = ip;
    }

    public PersonIP(String ip) {
        this.ip = ip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

}
