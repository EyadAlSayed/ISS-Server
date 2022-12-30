package com.iss.info.security.system.repo;

import com.iss.info.security.system.model.PersonSessionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SessionKeyRepo extends JpaRepository<PersonSessionKey, Integer> {

    @Query("SELECT ps.sessionKey FROM PersonSessionKey ps WHERE ps.person.id = :userId")
    Optional<PersonSessionKey> getSessionKeyByUserId(int userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM PersonSessionKey ps WHERE ps.person.id = :userId")
    void deleteUserSessionKey(int userId);

    @Modifying
    @Transactional
    @Query("UPDATE PersonSessionKey ps SET ps.sessionKey = :sessionKey WHERE ps.person.id = :userId")
    void updateUserSessionKey(int userId, String sessionKey);
}
