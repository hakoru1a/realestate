package com.dpdc.realestate.service.impl;

import com.dpdc.realestate.exception.BodyBadRequestException;
import com.dpdc.realestate.exception.DataAlreadyExistException;
import com.dpdc.realestate.exception.NotFoundException;
import com.dpdc.realestate.handler.EntityCheckHandler;
import com.dpdc.realestate.models.entity.Package;
import com.dpdc.realestate.repository.PackageRepository;
import com.dpdc.realestate.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
public class PackageServiceImpl implements PackageService {

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private Environment env;


    @Override
    public Page<Package> getPackages(Pageable pageable) {
        return packageRepository.findAll(pageable);
    }

    @Override
    public Package addPackage(Package pack) {
        boolean isExistPackageName = packageRepository.existsByPackageName(pack.getPackageName());
        if (isExistPackageName){
            throw new DataAlreadyExistException(null);
        }
        return packageRepository.save(pack);
    }

    @Override
    public void deletePackage(Integer packageId) {
        EntityCheckHandler.checkEntityExistById(packageRepository, packageId);
        packageRepository.deleteById(packageId);
    }

    @Override
    public Package updatePackage(Package pack, Integer packageId) {
        if (!Objects.equals(packageId, pack.getId())) {
                throw new BodyBadRequestException(env.getProperty("api.notify.bad_request"));
        }
        EntityCheckHandler.checkEntityExistById(packageRepository, pack.getId());
        return packageRepository.save(pack);
    }
}
