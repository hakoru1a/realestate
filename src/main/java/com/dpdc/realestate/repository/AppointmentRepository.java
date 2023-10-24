package com.dpdc.realestate.repository;

import com.dpdc.realestate.models.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Set;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    Set<Appointment> findByCustomerId(Integer customerId);

    Set<Appointment> findByCustomerIdAndAppointmentDateBetween(Integer customerId, Instant start, Instant end);

    Set<Appointment> findByUserIdAndAppointmentDateBetween(Integer staffId, Instant start, Instant end);


    Page<Appointment> findByIsCancelAndUserIsNull(Boolean isCancel,  Pageable pageable);

    Page<Appointment> findByUserId(Integer userId,  Pageable pageable);

    @Query("SELECT a FROM Appointment a WHERE a.user IS NULL")
    Set<Appointment> findAppointmentsWithNullUser();

}
