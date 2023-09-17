package com.dpdc.realestate.specifications;

import com.dpdc.realestate.models.entity.Property;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class PropertySpecifications {
    public static Specification<Property> propertyNameLike(String propertyName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("propertyName"), "%" + propertyName + "%");
    }

    public static Specification<Property> categoryIdEqual(Integer categoryId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Property> priceGreaterThanOrEqualTo(BigDecimal priceFrom) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("price"), priceFrom);
    }

    public static Specification<Property> priceLessThanOrEqualTo(BigDecimal priceTo) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("price"), priceTo);
    }

    public static Specification<Property> cityLike(String city) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("location").get("city"), "%" + city + "%");
    }

    public static Specification<Property> streetLike(String street) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("location").get("street"), "%" + street + "%");
    }

    public static Specification<Property> districtLike(String district) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("location").get("district"), "%" + district + "%");
    }


}
