package com.dpdc.realestate.service;

import com.dpdc.realestate.models.entity.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface PropertyService {

    Property addProperty(Property property);

    boolean deleteProperty(Property property);

    Property updateProperty(Property property);

    Property findById(Integer id);

    void softDeletePropertyById(Integer id);

    void hardDeletePropertyById(Integer id);


    Page<Property> getProperties(String propertyName, Integer categoryId,
                                 BigDecimal priceFrom, BigDecimal priceTo, String city,
                                 String street, String district, Pageable pageable);
}
