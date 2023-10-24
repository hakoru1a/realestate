package com.dpdc.realestate.repository;

import com.dpdc.realestate.models.entity.Aminitie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AminitieRepository extends JpaRepository<Aminitie, Integer> {
}
