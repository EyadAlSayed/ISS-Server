package com.iss.info.security.system.repo;

import com.iss.info.security.system.model.PersonMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepo extends JpaRepository<PersonMessage, Integer> {
    @Query("SELECT pm From PersonMessage pm " +
            "WHERE ( pm.fromUser =:fromUser " +
            "OR pm.fromUser =:toUser ) " +
            "AND ( pm.toUser =:toUser " +
            "OR pm.toUser =:fromUser ) ")
    List<PersonMessage> findByFromUserAndToUser(String fromUser, String toUser);
}
