package com.iss.info.security.system.repo;

import com.iss.info.security.system.model.PersonIP;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonIpRepo extends JpaRepository<PersonIP,Integer> {

}
