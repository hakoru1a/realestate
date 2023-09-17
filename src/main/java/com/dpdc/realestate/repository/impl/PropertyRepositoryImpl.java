package com.dpdc.realestate.repository.impl;

import com.dpdc.realestate.models.entity.Property;
import com.dpdc.realestate.repository.custom.PropertyRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;


@Repository
public class PropertyRepositoryImpl implements PropertyRepositoryCustom {
    @Override
    public Page<Property> getProperties(String propertyName, Integer categoryId, BigDecimal priceFrom, BigDecimal priceTo, String city, String street, String district, Pageable pageable) {
        return null;
    }
}
