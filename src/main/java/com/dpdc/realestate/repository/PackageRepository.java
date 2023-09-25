package com.dpdc.realestate.repository;

import com.dpdc.realestate.models.entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PackageRepository extends JpaRepository<Package, Integer> {
    boolean existsByPackageName(String packageName);


}
