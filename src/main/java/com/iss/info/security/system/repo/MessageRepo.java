package com.iss.info.security.system.repo;

import com.iss.info.security.system.model.PersonMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepo extends JpaRepository<PersonMessage, Integer> {

    List<PersonMessage> findByFromUserAndToUser(@Param("fromUser") String fromUser, @Param("toUser") String toUser);
}
