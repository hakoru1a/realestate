package com.dpdc.realestate.apis;


import com.dpdc.realestate.dto.ModelResponse;
import com.dpdc.realestate.models.entity.Appointment;
import com.dpdc.realestate.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/appointments/")
public class AppointmentAPI {

    @Autowired
    private Environment env;

    @Autowired
    private AppointmentService appointmentService;


    @GetMapping("/{customerId}/")
    public ResponseEntity<ModelResponse> getCustomerAppointments(@PathVariable Integer customerId){
        Set<Appointment> appointments =  appointmentService.getAppointments(customerId);
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                appointments), HttpStatus.OK);
    }

    @GetMapping("/staff/")
    public ResponseEntity<ModelResponse> getAllAppointments(
            @RequestParam(required = false, defaultValue = "1") String page
    ){
        Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1 , Integer.parseInt(env.getProperty("app.page.size")));
        Page<Appointment> appointments =  appointmentService.getAppointments(false, pageable);
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                appointments), HttpStatus.OK);
    }

    @GetMapping("/{staffId}/staff/")
    public ResponseEntity<ModelResponse> getAllAppointmentsByStaffId(
            @PathVariable Integer staffId,
            @RequestParam(required = false, defaultValue = "1") String page
    ){
        Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1 , Integer.parseInt(env.getProperty("app.page.size")));
        Page<Appointment> appointments =  appointmentService.getAppointments(staffId, pageable);
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                appointments), HttpStatus.OK);
    }

    @DeleteMapping("/{appointmentId}/{customerId}/")
    public ResponseEntity<ModelResponse> deleteCustomerAppointment(@PathVariable Integer appointmentId,
                                                                   @PathVariable Integer customerId){
        appointmentService.deleteAppointmentId(appointmentId, customerId);
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                null), HttpStatus.OK);
    }

    @PatchMapping("/{appointmentId}/{customerId}/")
    public ResponseEntity<ModelResponse> cancelCustomerAppointment(@PathVariable Integer appointmentId,
                                                                   @PathVariable Integer customerId){
        Appointment updatedAppointment = appointmentService.cancelAppointmentInteger(appointmentId, customerId);
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                updatedAppointment), HttpStatus.OK);
    }

    @PostMapping("/{appointmentId}/{staffId}/staff/")
    public ResponseEntity<ModelResponse> pickupAppointment(@PathVariable Integer appointmentId,
                                                           @PathVariable Integer staffId){
        Appointment updatedAppointment = appointmentService.pickupAppointment(appointmentId, staffId);
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                updatedAppointment), HttpStatus.OK);
    }


}
