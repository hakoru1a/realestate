package com.dpdc.realestate.service;

import com.dpdc.realestate.models.entity.Package;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PackageService {
    Page<Package> getPackages(Pageable pageable);

    Package addPackage(Package pack);

    void deletePackage(Integer packageId);

    Package updatePackage(Package pack, Integer packageId);
}
