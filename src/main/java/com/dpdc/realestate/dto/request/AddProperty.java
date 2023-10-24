package com.dpdc.realestate.dto.request;

import com.dpdc.realestate.models.entity.Aminitie;
import com.dpdc.realestate.models.entity.Customer;
import com.dpdc.realestate.models.enumerate.Purpose;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;


@Getter
@Setter
public class AddProperty {
    private Integer id;

    private String propertyName;

    private Customer customer;

    private BigDecimal price;

    private Integer bed;

    private Integer rentPeriod;

    private Integer bath;

    private Integer garage;

    private Integer kitchen;

    private BigDecimal area;

    private String latitude;

    private String longitude;

    private String slug;

    private Purpose purpose;

    private String propertyType;

    private String address;

    private String description;

    private Set<String> aminities;



}
