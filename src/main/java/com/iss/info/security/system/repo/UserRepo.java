package com.iss.info.security.system.repo;

import com.iss.info.security.system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Long> {
}
