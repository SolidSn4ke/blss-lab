package com.example.blsslab.model.repos;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.blsslab.model.entity.HousingEntity;
import com.example.blsslab.model.dto.RequestStatus;

@Repository
public interface HousingRepository extends JpaRepository<HousingEntity, Long>, JpaSpecificationExecutor<HousingEntity> {

    @Query("select h from HousingEntity h join fetch h.owner join fetch h.address")
    Page<HousingEntity> findAllWithJoinFetch(Specification<HousingEntity> spec, Pageable pageable);

    @Query("select h from HousingEntity h where h.status = :status")
    List<HousingEntity> findAllByStatus(RequestStatus status);
}
