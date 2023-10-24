package com.dpdc.realestate.service.impl;

import com.dpdc.realestate.models.entity.Aminitie;
import com.dpdc.realestate.repository.AminitieRepository;
import com.dpdc.realestate.service.AminitieService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AminitieServiceImpl implements AminitieService {

    private final AminitieRepository aminitieRepository;

    public AminitieServiceImpl(AminitieRepository aminitieRepository) {
        this.aminitieRepository = aminitieRepository;
    }

    @Override
    public void saveAll(Set<Aminitie> aminities) {
        aminitieRepository.saveAll(aminities);
    }
}
