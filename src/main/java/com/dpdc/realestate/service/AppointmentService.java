package com.dpdc.realestate.service;

import com.dpdc.realestate.models.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface AppointmentService {

    Set<Appointment> getAppointments(Integer customerId);

    void deleteAppointmentId(Integer appointmentId, Integer customerId);

    Appointment cancelAppointmentInteger(Integer appointmentId, Integer customerId);

    Page<Appointment> getAppointments(Boolean isCancel, Pageable pageable);

    Page<Appointment> getAppointments(Integer staffId, Pageable pageable);


    Appointment pickupAppointment(Integer appointmentId, Integer staffId);
}
