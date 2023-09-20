package com.dpdc.realestate.service.impl;

import com.dpdc.realestate.exception.ForbiddenException;
import com.dpdc.realestate.exception.NotFoundException;
import com.dpdc.realestate.handler.EntityCheckHandler;
import com.dpdc.realestate.models.entity.Customer;
import com.dpdc.realestate.models.entity.Property;
import com.dpdc.realestate.models.entity.User;
import com.dpdc.realestate.models.enumerate.Role;
import com.dpdc.realestate.repository.PropertyRepository;
import com.dpdc.realestate.repository.UserRepository;
import com.dpdc.realestate.service.CustomerService;
import com.dpdc.realestate.service.PropertyService;
import com.dpdc.realestate.service.UserService;
import com.dpdc.realestate.specifications.PropertySpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.parser.Entity;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class PropertyServiceImpl implements PropertyService {
    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserRepository userRepository;


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
        isOwnerProperty(property.getId());
        return propertyRepository.save(property);
    }

    @Override
    public Property findById(Integer id) {
        return EntityCheckHandler.checkEntityExistById(propertyRepository, id);
    }

    @Override
    public void softDeletePropertyById(Integer id, Boolean isDeleted)  {
        isOwnerProperty(id);
        propertyRepository.softDeletePropertyById(id, isDeleted);
    }

    @Override
    public void hardDeletePropertyById(Integer id) {
        EntityCheckHandler.checkEntityExistById(propertyRepository, id);
        propertyRepository.deleteById(id);
    }
    private void isOwnerProperty(Integer id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Property property = EntityCheckHandler.checkEntityExistById(propertyRepository,id);
        if (authentication.getAuthorities().stream().anyMatch(auth ->
                "ROLE_CUSTOMER".equals(auth.getAuthority()))){
            Customer customer = customerService.getCurrentCredential();
            if (customer.getProperties().stream().noneMatch(p -> p.getId().equals(property.getId()))) {
                throw new ForbiddenException("Access denied: Property does not belong to the current customer");
            }
        }
    }

    @Override
    public Page<Property> getProperties(String propertyName, Integer categoryId, BigDecimal priceFrom, BigDecimal priceTo, String city, String street, String district, Pageable pageable) {
        Specification<Property> spec = Specification.where((root, query, criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("isDeleted"), false),
                        criteriaBuilder.equal(root.get("isActive"), true)
                ));


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

    @Override
    public Property updatePropertyActiveStatus(Integer propertyId, Boolean isActive) {
        EntityCheckHandler.checkEntityExistById(propertyRepository, propertyId);
        propertyRepository.updatePropertyActiveStatus(propertyId,isActive);
        return findById(propertyId);
    }

    @Override
    public Page<Property> findMyProperties(Integer customerId, Pageable pageable ){
        return propertyRepository.findAllByCustomerId(customerId, pageable);
    }

    @Override
    public void assignStaffToProperty(Set<Integer> staffIds, Integer propertyId) {
        Property property = EntityCheckHandler.checkEntityExistById(propertyRepository, propertyId);
        // Get the current staff members assigned to the property
        Set<User> staffMembers = property.getStaffs();
        // Find the IDs of staff members already assigned to the property
        Set<Integer> existingStaffIds = staffMembers.stream()
                .map(User::getId)
                .collect(Collectors.toSet());// [1,2,3]
        // Calculate the staff IDs to add (newStaffIds) and remove (staffIdsToRemove)
        Set<Integer> newStaffIds = new HashSet<>(staffIds); // [1,2,5]
        Set<Integer> staffIdsToRemove = new HashSet<>(existingStaffIds);

        newStaffIds.removeAll(existingStaffIds); // IDs to add [5]
        staffIdsToRemove.removeAll(staffIds);    // IDs to remove [3]

        // Remove staff members from the property
        staffMembers.removeIf(s -> staffIdsToRemove.contains(s.getId()));

        // Add new staff members to the property
        for (Integer newStaffId : newStaffIds) {
            Optional<User> staffOptional = userRepository.findByIdAndRoleId(newStaffId, Role.STAFF.getValue());
            User staff = staffOptional.orElseThrow(() ->
                    new NotFoundException("User not found with ID: " + newStaffId)
            );
            staffMembers.add(staff);
        }
        property.setStaffs(staffMembers);
        // Save the updated property
        propertyRepository.save(property);
    }


}
