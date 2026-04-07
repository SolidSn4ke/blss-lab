package com.example.blsslab.model.mysql.repos;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.blsslab.model.dto.RequestStatus;
import com.example.blsslab.model.mysql.entity.BookingEntity;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long>, JpaSpecificationExecutor<BookingEntity> {

        @Query("select b from BookingEntity b")
        Page<BookingEntity> findAllWithJoinFetch(Specification<BookingEntity> spec, Pageable pageable);

        List<BookingEntity> findAllByHousingIdAndStatus(
                        Long housingId,
                        RequestStatus status);
}
