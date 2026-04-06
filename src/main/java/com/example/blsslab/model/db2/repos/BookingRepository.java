package com.example.blsslab.model.db2.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.blsslab.model.db2.entity.BookingEntity;
import com.example.blsslab.model.dto.RequestStatus;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long>, JpaSpecificationExecutor<BookingEntity> {

        List<BookingEntity> findAllByHousingIdAndStatus(
                        Long housingId,
                        RequestStatus status);
}
