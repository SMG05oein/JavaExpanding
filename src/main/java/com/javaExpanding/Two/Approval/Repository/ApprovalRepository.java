package com.javaExpanding.Two.Approval.Repository;

import com.javaExpanding.Two.Approval.Database.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Integer> {
}
