package com.javaExpanding.Two.User.Repository;

import com.javaExpanding.Two.User.Database.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByUserEmail(String email);
}
