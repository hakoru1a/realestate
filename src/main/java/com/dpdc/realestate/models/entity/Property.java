package com.dpdc.realestate.models.entity;

import com.dpdc.realestate.validator.anotation.ValidLocation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "property", schema = "realestate", indexes = {
        @Index(name = "location_id", columnList = "location_id", unique = true),
        @Index(name = "category_id", columnList = "category_id")
})
public class Property implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull(message = "Tên toà nhà bắt buộc")
    @Column(name = "property_name", nullable = false)
    private String propertyName;

    @Column(name = "is_active", insertable = false , updatable = false)
    private Boolean isActive;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    @ValidLocation
    private Location location;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "price", precision = 10, scale = 2)
    @NotNull(message = "Giá là bắt buộc")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal price;

    @Lob
    @NotNull(message = "Mô tả là bắt buộc")
    @Column(name = "description")
    private String description;


    @JsonIgnore
    @Column(name = "is_deleted", insertable = false, updatable = false)
    private Boolean isDeleted;


    @JsonIgnore
    @Column(name = "created_at", insertable = false , updatable = false)
    private Instant createdAt;

    @JsonIgnore
    @Column(name = "modified_at", insertable = false , updatable = false)
    private Instant modifiedAt;

    public Property(Integer id) {
        this.id = id;
    }

    public Property() {

    }




    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "manageproperty",
            joinColumns = @JoinColumn(name = "property_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> staffs;


    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "wishlist",
            joinColumns = @JoinColumn(name = "property_id"),
            inverseJoinColumns = @JoinColumn(name = "customer_id")
    )
    private Set<Property> wishlist;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;


}
