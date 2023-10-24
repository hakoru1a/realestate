package com.dpdc.realestate.service.impl;

import com.dpdc.realestate.exception.DeleteDataException;
import com.dpdc.realestate.exception.ForbiddenException;
import com.dpdc.realestate.exception.RejectException;
import com.dpdc.realestate.handler.EntityCheckHandler;
import com.dpdc.realestate.models.entity.Appointment;
import com.dpdc.realestate.models.entity.Customer;
import com.dpdc.realestate.models.entity.User;
import com.dpdc.realestate.repository.*;
import com.dpdc.realestate.service.AppointmentService;
import com.dpdc.realestate.service.CustomerService;
import com.dpdc.realestate.service.UserService;
import com.dpdc.realestate.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Set;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private Environment env;
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private Utils utils;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Override
    public Set<Appointment> getAppointments(Integer customerId) {
        Instant now = Instant.now();
        Customer customer = customerService.getCurrentCredential();
        if (!customer.getId().equals(customerId)){
            throw new ForbiddenException("Access denied: Appointments does not belong to the current customer");
        }
        // Tìm thời điểm bắt đầu của tuần sau (ngày thứ Hai tiếp theo)
        Instant nextMonday = now.atZone(ZoneId.of("UTC"))
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .toInstant();

        // Tìm thời điểm kết thúc của tuần sau (ngày Chủ Nhật tiếp theo)
        Instant nextSunday = nextMonday.plus(7, ChronoUnit.DAYS);
        return appointmentRepository.findByCustomerIdAndAppointmentDateBetween(customerId, nextMonday,nextSunday );
    }
    @Override
    public Set<Appointment> getAppointmentsStaff(Integer staffId) {
        Instant now = Instant.now();
        User user = userService.getCurrentCredential();
        if (!user.getId().equals(staffId)){
            throw new ForbiddenException("Access denied: Appointments does not belong to the current customer");
        }
        // Tìm thời điểm bắt đầu của tuần sau (ngày thứ Hai tiếp theo)
        Instant nextMonday = now.atZone(ZoneId.of("UTC"))
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .toInstant();

        // Tìm thời điểm kết thúc của tuần sau (ngày Chủ Nhật tiếp theo)
        Instant nextSunday = nextMonday.plus(7, ChronoUnit.DAYS);
        return appointmentRepository.findByUserIdAndAppointmentDateBetween(staffId, nextMonday,nextSunday);
    }
    @Override
    public void deleteAppointmentId(Integer appointmentId, Integer customerId) {
        Appointment appointment = isMyAppointment(appointmentId, customerId);
        if (appointment.getUser() == null)
            appointmentRepository.deleteById(appointmentId);
        else throw new DeleteDataException("Access denied: User is handling this appointment, please cancel it");
    }

    @Override
    public Appointment cancelAppointmentInteger(Integer appointmentId, Integer customerId) {
        Appointment appointment = isMyAppointment(appointmentId, customerId);
        appointment.setIsCancel(true);
        return appointmentRepository.save(appointment);
    }

    @Override
    public Page<Appointment> getAppointments(Boolean isCancel, Pageable pageable) {
        return appointmentRepository.findByIsCancelAndUserIsNull(isCancel, pageable);
    }

    @Override
    public Page<Appointment> getAppointments(Integer staffId, Pageable pageable) {
        return appointmentRepository.findByUserId(staffId, pageable);
    }

    @Override
    public Set<Appointment> getAppointmentsWithNullUser() {
        return appointmentRepository.findAppointmentsWithNullUser();
    }

    @Override
    public Appointment pickupAppointment(Integer appointmentId, Integer staffId) {
        Appointment appointment = EntityCheckHandler.checkEntityExistById(appointmentRepository, appointmentId);
        if (appointment.getIsCancel()){
            throw new RejectException("Lịch đã bị khách hàng cancel chọn lịch khác");
        }

        User user =  EntityCheckHandler.checkEntityExistById(userRepository, staffId);
        // Check conflict staff
        Set<Appointment> appointments = user.getAppointments();
        int myShift = Utils.calculateAppointmentShift(appointment.getAppointmentDate());
        for (Appointment a: appointments){
            int shift =  Utils.calculateAppointmentShift(a.getAppointmentDate());
            if (myShift == shift){
                throw new RejectException("Ngày này đã có lịch");
            }
        }
        appointment.setUser(user);
        return appointmentRepository.save(appointment);
    }

    private Appointment isMyAppointment(Integer appointmentId, Integer customerId){
        Appointment appointment = EntityCheckHandler.checkEntityExistById(appointmentRepository, appointmentId);
        if(!appointment.getCustomer().getId().equals(customerId)){
            throw new ForbiddenException("Access denied: Appointments does not belong to the current customer");
        }
        return appointment;
    }



}
