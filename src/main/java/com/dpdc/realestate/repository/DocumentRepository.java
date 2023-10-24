package com.dpdc.realestate.repository;

import com.dpdc.realestate.models.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Integer> {

    Integer countByPropertyId(Integer propertyId);
}
