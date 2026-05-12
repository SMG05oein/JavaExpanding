package com.javaExpanding.Two.Reservation.Repository;

import com.javaExpanding.Two.Reservation.Database.Reservation;
import com.javaExpanding.Two.User.Database.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    Page<Reservation> findByUser(Users user, Pageable pageable);
}
