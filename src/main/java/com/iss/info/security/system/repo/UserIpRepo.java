package com.iss.info.security.system.repo;

import com.iss.info.security.system.model.Person;
import com.iss.info.security.system.model.PersonIP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserIpRepo extends JpaRepository<PersonIP,Long> {

}
