package com.iss.info.security.system.repo;

import com.iss.info.security.system.model.PersonPublicKey;
import com.iss.info.security.system.model.PersonSessionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PublicKeyRepo extends JpaRepository<PersonPublicKey, Integer> {

    @Query("SELECT pk FROM PersonPublicKey pk WHERE pk.person.id = :userId")
    Optional<PersonPublicKey> getPublicKeyByUserId(int userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM PersonPublicKey pk WHERE pk.person.id = :userId")
    void deleteUserPublicKey(int userId);

    @Modifying
    @Transactional
    @Query("UPDATE PersonPublicKey pk SET pk.publicKey = :publicKey WHERE pk.person.id = :userId")
    void updateUserPublicKey(int userId, String publicKey);
}
