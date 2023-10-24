package com.dpdc.realestate.service.impl;

import com.dpdc.realestate.dto.response.BillDTO;
import com.dpdc.realestate.exception.BodyBadRequestException;
import com.dpdc.realestate.exception.ForbiddenException;
import com.dpdc.realestate.exception.RejectException;
import com.dpdc.realestate.handler.EntityCheckHandler;
import com.dpdc.realestate.models.entity.*;
import com.dpdc.realestate.models.enumerate.PaymentType;
import com.dpdc.realestate.repository.AppointmentRepository;
import com.dpdc.realestate.repository.CustomerRepository;
import com.dpdc.realestate.repository.PaymentRepository;
import com.dpdc.realestate.repository.PropertyRepository;
import com.dpdc.realestate.service.AppointmentService;
import com.dpdc.realestate.service.CustomerService;
import com.dpdc.realestate.service.PaymentService;
import com.dpdc.realestate.service.RegisterPackageService;
import com.dpdc.realestate.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private RegisterPackageService registerPackageService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private Environment env;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private AppointmentService appointmentService;


    @Override
    public Payment createPaymentFromBuyTurn(Integer packageId, Integer customerId,  Integer quantity) {
        BillDTO billDTO = registerPackageService.checkBill(packageId, customerId, quantity);
        Customer customer = EntityCheckHandler.checkEntityExistById(customerRepository, customerId);
        CustomerPackageRegistration customerPackageRegistration = registerPackageService
                .savePackageRegister(packageId, customerId, quantity);
        if (billDTO != null){
            Payment payment = new Payment();
            payment.setPaymentDate(Instant.now());
            payment.setPaymentMethod("MOMO");
            payment.setAmount(customerPackageRegistration.getServicePackage()
                    .getPrice().multiply(BigDecimal.valueOf(quantity)));
            payment.setType(PaymentType.BUY_TURN.name());
            payment.setCustomer(customer);
            Payment pay =  paymentRepository.save(payment);
            customerPackageRegistration.setPayment(payment);
            customer.setTimes(customer.getTimes() + billDTO.getPack().getTimes());
            customerRepository.save(customer);
            return pay;
        }
        throw new BodyBadRequestException(env.getProperty("api.notify.bad_request"));
    }

    @Override
    public Payment createPaymentFromBookAppointment(Appointment appointment) {
        isValidAppointment(appointment);
        Appointment myAppointment = appointmentRepository.save(appointment);
        Payment payment = new Payment();
        payment.setPaymentDate(Instant.now());
        payment.setPaymentMethod("MOMO");
        payment.setAmount(BigDecimal.valueOf(Long.parseLong(env.getProperty("app.booking_fee"))));
        payment.setType(PaymentType.BOOK_APPOINTMENT.name());
        payment.setCustomer(appointment.getCustomer());
        Payment pay =  paymentRepository.save(payment);
        myAppointment.setPayment(payment);
        myAppointment.setIsCancel(false);
        appointmentRepository.save(myAppointment);
        return pay;
    }


    @Override
    public void isValidAppointment(Appointment appointment) {
        Appointment conflictAppointment = checkConflictAppointment(appointment);
        if (conflictAppointment != null){
            throw new RejectException("Trùng lịch");
        }
        EntityCheckHandler.checkEntityExistById(propertyRepository, appointment.getProperty().getId());
    }


    @Override
    public Set<PaymentData> getPayments(Integer customerId) {
        isMyPaymentOrAdmin(customerId);
        return paymentRepository.findPaymentsByPaymentTypeAndCustomerId(PaymentType.BUY_TURN.name(), customerId);
    }
    private Appointment checkConflictAppointment(Appointment appointment) {
        Instant time = appointment.getAppointmentDate();
        Set<Appointment> existAppointments = appointmentRepository
                .findByCustomerId(appointment.getCustomer().getId());
        Integer currentShift = Utils.calculateAppointmentShift(time);
        if (currentShift == -1)
            throw new RejectException("Sai lịch");
        // Biến để kiểm tra xem có trùng ngày không
        boolean isDateConflict = false;
        // Kiểm tra xem cuộc hẹn có trùng ngày với các cuộc hẹn khác hay không
        for (Appointment existingAppointment : existAppointments) {
            if (Utils.isSameDay(time, existingAppointment.getAppointmentDate())) {
                isDateConflict = true;
                break; // Thoát khỏi vòng lặp nếu trùng ngày
            }
        }

        // Nếu trùng ngày, tính toán và so sánh ca
        if (isDateConflict) {
            for (Appointment existingAppointment : existAppointments) {
                Integer existingShift = Utils.calculateAppointmentShift(existingAppointment.getAppointmentDate());
                if (currentShift.equals(existingShift)) {
                    // Nếu trùng ca, trả về cuộc hẹn gây xung đột
                    return existingAppointment;
                }
            }
        }

        // Nếu không trùng ngày hoặc không trùng ca, trả về null
        return null;
    }


    private void  isMyPaymentOrAdmin(Integer customerId){
        Customer currentCustomer = customerService.getCurrentCredential();
        if(currentCustomer != null){
            if (!Objects.equals(currentCustomer.getId(), customerId)){
                throw new ForbiddenException("Access denied: Comment does not belong to the current customer");
            }
        }
    }

}
