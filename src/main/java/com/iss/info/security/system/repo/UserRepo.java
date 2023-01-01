package com.iss.info.security.system.repo;

import com.iss.info.security.system.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<Person, Long> {

    Optional<Person> findByPhoneNumber(String phoneNumber);

    Optional<Person> getUserByPhoneNumber(String phoneNumber);

    @Query("SELECT p FROM Person p JOIN PersonIP ip WHERE p.personIp.id = ip.id" +
            " AND ip.ip = :ipAddress")
    Optional<Person> getUserByIPAddress(String ipAddress);
}
