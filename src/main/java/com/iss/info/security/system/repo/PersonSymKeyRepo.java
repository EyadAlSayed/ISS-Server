package com.iss.info.security.system.repo;

import com.iss.info.security.system.model.PersonSymmetricKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonSymKeyRepo extends JpaRepository<PersonSymmetricKey,Integer> {
}
