package com.iss.info.security.system.repo;

import com.iss.info.security.system.model.Person;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<Person,Long> {

    Optional<Person> findByPhoneNumber(String phoneNumber);

    Optional<Person> getUserByPhoneNumber(String phoneNumber);


    Optional<Person> findByPhoneNumberAndPassword(String phoneNumber,String password);

    String findPhoneNumberByUserIp(String ip);
}
