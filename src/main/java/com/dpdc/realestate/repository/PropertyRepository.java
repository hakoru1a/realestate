package com.dpdc.realestate.repository;

import com.dpdc.realestate.models.entity.Customer;
import com.dpdc.realestate.models.entity.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Integer>,
        JpaSpecificationExecutor<Property> {
    @Modifying
    @Query("UPDATE Property p SET p.isDeleted = :isDeleted WHERE p.id = :propertyId")
    void softDeletePropertyById(@Param("propertyId") Integer propertyId, @Param("isDeleted") Boolean isDeleted);

    List<Property> findByIsDeletedTrue();

    List<Property> findByIsActiveTrue();


    @Modifying(clearAutomatically = true)
    @Query("UPDATE Property p SET p.isActive = :isActive WHERE p.id = :propertyId")
    void updatePropertyActiveStatus(@Param("propertyId") Integer propertyId, @Param("isActive") Boolean isActive);

    Page<Property> findAllByCustomerId(Integer customerId, Pageable pageable);

    Set<Property> findAllByCustomerIdAndIsDeletedIsFalse(Integer customerId );
    @Query("SELECT p FROM Property p WHERE p.id IN" +
            " (SELECT w.property.id FROM Wishlist w WHERE w.customer.id = :customerId)")
    Page<Property> findAllPropertiesInWishlistByCustomerId(@Param("customerId") Integer customerId,
                                                           Pageable pageable);

    @Query("SELECT p FROM Property p WHERE p.id IN" +
            " (SELECT w.property.id FROM Wishlist w WHERE w.customer.id = :customerId)")
    Set<Property> findAllPropertiesInWishlistByCustomerId(@Param("customerId") Integer customerId
                                                           );
    Optional<Property> findByCustomerAndId(Customer customer, Integer id);

    Set<Property> findByIsActiveFalse();

    @Query(value = "SELECT p.* FROM Property p LEFT JOIN ManageProperty mp ON p.id = mp.property_id WHERE mp.id IS NULL", nativeQuery = true)
    List<Property> findUnmanagedProperties();

    Optional<Property> findByIsDeletedFalseAndId(Integer id);
}
