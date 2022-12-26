package com.iss.info.security.system.repo;

import com.iss.info.security.system.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<Person,Long> {

    Optional<Person> findByPhoneNumber(String phoneNumber);

    Optional<Person> getUserByPhoneNumber(String phoneNumber);

    List<Person> findByPersonParentId(int userParentId);
}
