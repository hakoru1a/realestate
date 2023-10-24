package com.dpdc.realestate.repository;

import com.dpdc.realestate.models.entity.Manageproperty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface ManagePropertyRepository extends JpaRepository<Manageproperty, Integer> {
    Integer countByUserId(Integer userId);

    void deleteByPropertyIdAndUserId( Integer propertyId, Integer userId);
}
