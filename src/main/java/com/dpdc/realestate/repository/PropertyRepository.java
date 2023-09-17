package com.dpdc.realestate.repository;

import com.dpdc.realestate.models.entity.Property;
import com.dpdc.realestate.repository.custom.PropertyRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
@Transactional
public interface PropertyRepository extends JpaRepository<Property, Integer>, JpaSpecificationExecutor<Property> {
    @Modifying
    @Query("UPDATE Property p SET p.isDeleted = true WHERE p.id = :propertyId")
    void softDeletePropertyById(@Param("propertyId") Integer propertyId);


    Page<Property> findAllByIsDeletedFalse(Pageable pageable);







    //    @Query("SELECT p FROM Property p " +
//            "LEFT JOIN p.location loc " +
//            "WHERE " +
//            "(:propertyName IS NULL OR p.propertyName LIKE %:propertyName%) AND " +
//            "(:categoryId IS NULL OR p.category.id = :categoryId) AND " +
//            "(:priceFrom IS NULL OR p.price >= :priceFrom) AND " +
//            "(:priceTo IS NULL OR p.price <= :priceTo) AND " +
//            "p.isDeleted = false AND " +
//            "(:city IS NULL OR loc.city LIKE %:city%) AND " +
//            "(:street IS NULL OR loc.street LIKE %:street%) AND " +
//            "(:district IS NULL OR loc.district LIKE %:district%)")
//    Page<Property> getProperties(
//            @Param("propertyName") String propertyName,
//            @Param("categoryId") Integer categoryId,
//            @Param("priceFrom") BigDecimal priceFrom,
//            @Param("priceTo") BigDecimal priceTo,
//            @Param("city") String city,
//            @Param("street") String street,
//            @Param("district") String district,
//            Pageable pageable
//    );
}
