package com.iss.info.security.system.repo;

import com.iss.info.security.system.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PersonRepo extends JpaRepository<Person,Integer> {



    boolean existsByPhoneNumber(String phoneNumber);
    Optional<Person> findPersonByPhoneNumber(String phoneNumber);

    Optional<Person> findByPhoneNumberAndPassword(String phoneNumber,String password);

    @Query("SELECT p.phoneNumber FROM Person p where p.personIp.ip =:ip")
    String findPhoneNumberByPersonIp(@Param("ip") String ip);
}
