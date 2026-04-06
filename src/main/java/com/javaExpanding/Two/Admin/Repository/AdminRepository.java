package com.javaExpanding.Two.Admin.Repository;

import com.javaExpanding.Two.Admin.Database.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByAdminIdOrAdminEmail(String adminId, String adminEmail);

    boolean existsByAdminId(String adminId);
}