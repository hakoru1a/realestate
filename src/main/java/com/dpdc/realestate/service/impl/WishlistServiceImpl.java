package com.dpdc.realestate.service.impl;

import com.dpdc.realestate.handler.EntityCheckHandler;
import com.dpdc.realestate.models.entity.Customer;
import com.dpdc.realestate.models.entity.Property;
import com.dpdc.realestate.repository.CustomerRepository;
import com.dpdc.realestate.repository.PropertyRepository;
import com.dpdc.realestate.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class WishlistServiceImpl implements WishlistService {


    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Override
    public Page<Property> getWishlist(Integer id, Pageable pageable) {
        Customer customer = EntityCheckHandler.checkEntityExistById(customerRepository, id);
        return propertyRepository.findAllPropertiesInWishlistByCustomerId(id, pageable);
    }

    @Override
    public Set<Property> getWishlist(Integer id) {
         EntityCheckHandler.checkEntityExistById(customerRepository, id);
        return propertyRepository.findAllPropertiesInWishlistByCustomerId(id);
    }

    @Override
    public Property addOrRemoveWishlist(Integer propertyId, Integer customerId) {
        Customer customer = EntityCheckHandler.checkEntityExistById(customerRepository, customerId);
        Property property = EntityCheckHandler.checkEntityExistById(propertyRepository, propertyId);
        if (customer.getWishlist().contains(property)) {
            customer.getWishlist().remove(property);
        } else {
            customer.getWishlist().add(property);
        }
        customerRepository.save(customer);
        return property;
    }


}
