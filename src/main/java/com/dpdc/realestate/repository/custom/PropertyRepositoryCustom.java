package com.dpdc.realestate.repository.custom;

import com.dpdc.realestate.models.entity.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface PropertyRepositoryCustom {
    Page<Property> getProperties(
            String propertyName,
            Integer categoryId,
            BigDecimal priceFrom,
            BigDecimal priceTo,
            String city,
            String street,
            String district,
            Pageable pageable
    );
}
