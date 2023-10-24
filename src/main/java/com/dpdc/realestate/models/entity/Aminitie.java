package com.dpdc.realestate.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "aminitie")
public class Aminitie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    private Property property;

    @Lob
    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private String name;

    public Aminitie(Integer id) {
        this.id = id;
    }

    public Aminitie() {

    }
}