package com.dpdc.realestate.repository;

import com.dpdc.realestate.models.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MediaRepository extends JpaRepository<Media, Integer> {

    @Query(value = "SELECT COUNT(m.id) FROM media m " +
            "LEFT JOIN property p ON m.property_id = p.id " +
            "WHERE p.id = :propertyId AND m.media_type LIKE :mediaType",
            nativeQuery = true)
    Integer countMediaByPropertyIdAndMediaType(@Param("propertyId") Integer propertyId, @Param("mediaType") String mediaType);

}
