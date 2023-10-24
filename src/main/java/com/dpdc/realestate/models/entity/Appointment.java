package com.dpdc.realestate.models.entity;

import com.dpdc.realestate.validator.anotation.FutureWeek;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "appointment", schema = "realestate", indexes = {
        @Index(name = "user_id", columnList = "user_id"),
        @Index(name = "customer_id", columnList = "customer_id"),
        @Index(name = "property_id", columnList = "property_id")
})
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;


    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "property_id")
    private Property property;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @FutureWeek
    @Column(name = "appointment_date")
    private Instant appointmentDate;

    @Column(name = "is_cancel", insertable = false)
    private Boolean isCancel;


    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "modified_at", insertable = false, updatable = false)
    private Instant modifiedAt;


    public Appointment(Integer propertyId, Integer customerId, Instant appointmentDate ){
        this.property = new Property(propertyId);
        this.customer = new Customer(customerId);
        this.appointmentDate = appointmentDate;
    }
    public Appointment(){

    }
}