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

    List<Property> getDeletedProperty();

    Property updateProperty(Property property);

    Property findById(Integer id);

    Property getPropertyByIdAndDeleteFalse(Integer id);
     void softDeletePropertyById(Integer id, Boolean isDeleted);

    void hardDeletePropertyById(Integer propertyId);

    Set<Property> findByIsActiveFalse();

    List<Property> getAll();

    List<Property> getByIsActiveTrue();
    List<Property> getUnmanagedProperties();
    Page<Property> getProperties(String propertyName, Integer categoryId,
                                 BigDecimal priceFrom, BigDecimal priceTo, String city,
                                 String street, String district, Pageable pageable);

    Property updatePropertyActiveStatus(Integer propertyId,
                                    Boolean isActive);

    Page<Property> findMyProperties(Integer customerId, Pageable pageable );

    Set<Property> findMyProperties(Integer customerId);

    Set<Property> findMyPropertiesStaff(Integer staffId);

    void assignStaffToProperty(Set<Integer> staffId, Integer propertyId);

    void assignProperty(Integer propertyId, Integer staffId);
    void deleteAssign(Integer propertyId, Integer staffId);

}
