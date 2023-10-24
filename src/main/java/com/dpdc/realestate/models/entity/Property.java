package com.dpdc.realestate.models.entity;

import com.dpdc.realestate.models.enumerate.Purpose;
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

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "price", precision = 10, scale = 2)
    @NotNull(message = "Giá là bắt buộc")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal price;


    @Column(name = "bed")
    @NotNull(message = "Số phòng ngủ là bắt buộc")
    private Integer bed;

    @Column(name = "rent_period")
    private Integer rentPeriod;

    @Column(name = "bath")
    @NotNull(message = "bath là bắt buộc")
    private Integer bath;


    @Column(name = "garage")
    @NotNull(message = "garage là bắt buộc")
    private Integer garage;

    @Column(name = "kitchen")
    @NotNull(message = "kitchen là bắt buộc")
    private Integer kitchen;

    @Column(name = "area")
    @NotNull(message = "area là bắt buộc")
    private BigDecimal area;

    @Column(name = "latitude")
    @NotNull(message = "latitude là bắt buộc")
    private String latitude;

    @Column(name = "address")
    @NotNull(message = "address là bắt buộc")
    private String address;

    @Column(name = "longitude")
    @NotNull(message = "longitude là bắt buộc")
    private String longitude;

    @Column(name = "slug")
    private String slug;

    @Column(name = "purpose")
    @Enumerated(EnumType.STRING)
    private Purpose purpose;


    @Lob
    @Column(name = "description")
    private String description;


    @Lob
    @NotNull(message = "type là bắt buộc")
    @Column(name = "property_type")
    private String propertyType;

    @Column(name = "is_active", insertable = false , updatable = false)
    private Boolean isActive;

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

    @OneToMany(mappedBy = "property", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Aminitie> aminities;

    @OneToMany(mappedBy = "property", fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    private Set<Comment> comments;

    @OneToMany(mappedBy = "property", fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    private Set<Media> medias;

    @OneToMany(mappedBy = "property", fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    private Set<Document> documents;

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




}
