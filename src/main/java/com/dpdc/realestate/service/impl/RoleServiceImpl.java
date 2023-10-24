package com.dpdc.realestate.service.impl;

import com.dpdc.realestate.handler.EntityCheckHandler;
import com.dpdc.realestate.models.entity.Role;
import com.dpdc.realestate.repository.RoleRepository;
import com.dpdc.realestate.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Override
    public Role getRoleById(Integer id) {
        return EntityCheckHandler.checkEntityExistById(roleRepository, id);
    }
}
