package com.iss.info.security.system.repo;

import com.iss.info.security.system.model.PersonSymmetricKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PersonSymKeyRepo extends JpaRepository<PersonSymmetricKey,Integer> {

    @Query("SELECT sk FROM PersonSymmetricKey sk WHERE sk.id = :userId")
    Optional<PersonSymmetricKey> getSymmetricKeyByUserId(int userId);
}
