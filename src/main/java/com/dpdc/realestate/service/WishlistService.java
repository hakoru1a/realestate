package com.dpdc.realestate.service;

import com.dpdc.realestate.models.entity.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface WishlistService {
    Page<Property> getWishlist(Integer id, Pageable pageable);

    Property addOrRemoveWishlist(Integer propertyId, Integer customerId);


}
