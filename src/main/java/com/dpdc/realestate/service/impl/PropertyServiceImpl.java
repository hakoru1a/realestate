package com.dpdc.realestate.service.impl;

import com.dpdc.realestate.exception.ForbiddenException;
import com.dpdc.realestate.exception.NotFoundException;
import com.dpdc.realestate.exception.RejectException;
import com.dpdc.realestate.handler.EntityCheckHandler;
import com.dpdc.realestate.models.entity.Comment;
import com.dpdc.realestate.models.entity.Customer;
import com.dpdc.realestate.models.entity.Property;
import com.dpdc.realestate.models.entity.User;
import com.dpdc.realestate.models.enumerate.Role;
import com.dpdc.realestate.repository.CustomerRepository;
import com.dpdc.realestate.repository.ManagePropertyRepository;
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
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ManagePropertyRepository managePropertyRepository;


    @Override
    public Property addProperty(Property property) {
        Customer customer = EntityCheckHandler.checkEntityExistById(customerRepository, property.getCustomer().getId());
        if (customer.getTimes() == null || customer.getTimes() == 0)
            throw new RejectException("Not enough turns");
        customerService.setTurn(property.getCustomer().getId(),  1, true);
        return  propertyRepository.save(property);
    }

    @Override
    public boolean deleteProperty(Property property) {
        return false;
    }

    @Override
    public List<Property> getDeletedProperty() {
        return propertyRepository.findByIsDeletedTrue();
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
    public Property getPropertyByIdAndDeleteFalse(Integer id) {
        return propertyRepository.findByIsDeletedFalseAndId(id)
                .orElseThrow(() -> new NotFoundException("NOT FOUND"));
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

    @Override
    public Set<Property> findByIsActiveFalse() {
        return propertyRepository.findByIsActiveFalse();
    }

    @Override
    public List<Property> getAll() {
        return propertyRepository.findAll();
    }

    @Override
    public List<Property> getByIsActiveTrue() {
        return propertyRepository.findByIsActiveTrue();
    }

    @Override
    public List<Property> getUnmanagedProperties() {
        return propertyRepository.findUnmanagedProperties();
    }

    private void isOwnerProperty(Integer id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Property property = EntityCheckHandler.checkEntityExistById(propertyRepository,id);
        if (authentication.getAuthorities().stream().anyMatch(auth ->
                "ROLE_CUSTOMER".equals(auth.getAuthority()))){
            Customer customer = customerService.getCurrentCredential();
            Optional<Property> c = propertyRepository.findByCustomerAndId(customer, property.getId());
            if (c.isEmpty()) {
                throw new ForbiddenException("Access denied: Property does not belong to the current customer");
            }
        }
    }

    @Override
    public Page<Property> getProperties(String propertyName, Integer categoryId, BigDecimal
            priceFrom, BigDecimal priceTo, String city, String street, String district, Pageable pageable) {
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


        return propertyRepository.findAll(spec, pageable);
    }

    @Override
    public Property updatePropertyActiveStatus(Integer propertyId, Boolean isActive) {
        Property p = EntityCheckHandler.checkEntityExistById(propertyRepository, propertyId);
        propertyRepository.updatePropertyActiveStatus(propertyId,isActive);
        if (!isActive){
            Customer customer = p.getCustomer();
            customer.setTimes(customer.getTimes() - 1);
        }
        return findById(propertyId);
    }

    @Override
    public Page<Property> findMyProperties(Integer customerId, Pageable pageable ){
        return propertyRepository.findAllByCustomerId(customerId, pageable);
    }

    @Override
    public Set<Property> findMyProperties(Integer customerId) {
        return  propertyRepository.findAllByCustomerIdAndIsDeletedIsFalse(customerId);
    }

    @Override
    public Set<Property> findMyPropertiesStaff(Integer staffId) {
        User user = EntityCheckHandler.checkEntityExistById( userRepository, staffId);
        return user.getPropertyManage();
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

    @Override
    public void assignProperty(Integer propertyId, Integer staffId) {
        Integer count = managePropertyRepository.countByUserId(staffId);
        if (count > 10) {
            throw new RejectException("Quá 10 nhà đã được giao");
        }
        Property property = EntityCheckHandler.checkEntityExistById(propertyRepository, propertyId);
        User user = EntityCheckHandler.checkEntityExistById(userRepository, staffId);
        Set<Property> properties = user.getPropertyManage();
        properties.add(property);
        propertyRepository.save(property);

    }

    @Override
    public void deleteAssign(Integer propertyId, Integer staffId) {
        EntityCheckHandler.checkEntityExistById(propertyRepository, propertyId);
        EntityCheckHandler.checkEntityExistById(userRepository, staffId);
        managePropertyRepository.deleteByPropertyIdAndUserId(propertyId, staffId);
    }


}
