package com.example.blsslab.model.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.blsslab.model.entity.HousingEntity;

@Repository
public interface HousingRepository extends JpaRepository<HousingEntity, Long> {
    
}
