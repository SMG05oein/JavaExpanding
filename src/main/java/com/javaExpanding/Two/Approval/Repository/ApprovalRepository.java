package com.javaExpanding.Two.Approval.Repository;

import com.javaExpanding.Two.Approval.Database.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javaExpanding.Two.Reservation.Database.Reservation;
import java.util.Optional;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Integer> {
    Optional<Approval> findFirstByReservationOrderByAppIdxDesc(Reservation reservation);
}
