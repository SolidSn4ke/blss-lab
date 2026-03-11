package com.example.blsslab.model.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.blsslab.model.dto.RequestStatus;
import com.example.blsslab.model.entity.BookingEntity;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long>, JpaSpecificationExecutor<BookingEntity> {

        List<BookingEntity> findAllByHousingIdAndStatus(
                        Long housingId,
                        RequestStatus status);
}
