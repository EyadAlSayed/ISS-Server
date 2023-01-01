package com.iss.info.security.system.repo;

import com.iss.info.security.system.model.PersonContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface PersonContactRepo extends JpaRepository<PersonContact,Integer> {


    @Query("SELECT pc FROM PersonContact pc WHERE pc.person.id =:id")
    List<PersonContact> findAllPersonContactByPersonId(@Param("id")int  personId);
}
