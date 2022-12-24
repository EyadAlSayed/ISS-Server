package com.iss.info.security.system.repo;

import com.iss.info.security.system.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepo extends JpaRepository<Message,Integer> {
}
