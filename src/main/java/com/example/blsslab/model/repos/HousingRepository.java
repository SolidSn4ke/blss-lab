package com.example.blsslab.model.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.blsslab.model.entity.HousingEntity;
import com.example.blsslab.model.dto.RequestStatus;

@Repository
public interface HousingRepository extends JpaRepository<HousingEntity, Long> {

    @Query("select h from HousingEntity h where h.status = :status")
    List<HousingEntity> findAllByStatus(RequestStatus status);
}
