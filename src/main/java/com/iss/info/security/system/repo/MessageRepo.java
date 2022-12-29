package com.iss.info.security.system.repo;

import com.iss.info.security.system.model.PersonMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepo extends JpaRepository<PersonMessage,Integer> {

    List<PersonMessage> findByFromUserAndToUser(String fromUser,String toUser);
}
