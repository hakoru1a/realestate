package com.dpdc.realestate.service.impl;

import com.dpdc.realestate.models.entity.Property;
import com.dpdc.realestate.repository.PropertyRepository;
import com.dpdc.realestate.service.PropertyService;
import com.dpdc.realestate.specifications.PropertySpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class PropertyServiceImpl implements PropertyService {
    @Autowired
    private PropertyRepository propertyRepository;
    @Override
    public Property addProperty(Property property) {
        return  propertyRepository.save(property);
    }

    @Override
    public boolean deleteProperty(Property property) {
        return false;
    }

    @Override
    public Property updateProperty(Property property) {
        return propertyRepository.save(property);
    }

    @Override
    public Property findById(Integer id) {
        return propertyRepository.findById(id).orElse(null);
    }

    @Override
    public void softDeletePropertyById(Integer id) {
        propertyRepository.softDeletePropertyById(id);
    }

    @Override
    public void hardDeletePropertyById(Integer id) {
        propertyRepository.deleteById(id);
    }

    @Override
    public Page<Property> getProperties(String propertyName, Integer categoryId, BigDecimal priceFrom, BigDecimal priceTo, String city, String street, String district, Pageable pageable) {
        Specification<Property> spec = Specification.where(null);
//        spec = spec.and(PropertySpecifications.propertyNameLike(null));
//        .and(PropertySpecifications.categoryIdEqual(categoryId))
//        .and(PropertySpecifications.priceGreaterThanOrEqualTo(priceFrom))
//        .and(PropertySpecifications.cityLike(city))
//        .and(PropertySpecifications.streetLike(street))
//        .and(PropertySpecifications.districtLike(district))
//        .and(PropertySpecifications.priceLessThanOrEqualTo(priceTo));

        if (propertyName != null && !propertyName.isEmpty()) {
            spec = spec.and(PropertySpecifications.propertyNameLike(propertyName));
        }

        if (categoryId != null) {
            spec = spec.and(PropertySpecifications.categoryIdEqual(categoryId));
        }

        if (priceFrom != null) {
            spec = spec.and(PropertySpecifications.priceGreaterThanOrEqualTo(priceFrom));
        }

        if (priceTo != null) {
            spec = spec.and(PropertySpecifications.priceLessThanOrEqualTo(priceTo));
        }

        if (city != null && !city.isEmpty()) {
            spec = spec.and(PropertySpecifications.cityLike(city));
        }

        if (street != null && !street.isEmpty()) {
            spec = spec.and(PropertySpecifications.streetLike(street));
        }

        if (district != null && !district.isEmpty()) {
            spec = spec.and(PropertySpecifications.districtLike(district));
        }
        return propertyRepository.findAll(spec, pageable);
    }


}
