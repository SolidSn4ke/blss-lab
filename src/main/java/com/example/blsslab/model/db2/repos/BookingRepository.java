package com.example.blsslab.model.db2.repos;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.blsslab.model.db2.entity.BookingEntity;
import com.example.blsslab.model.dto.RequestStatus;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long>, JpaSpecificationExecutor<BookingEntity> {

        @Query("select b from BookingEntity b join fetch b.guest join fetch b.housing join fetch b.housing.address join fetch b.housing.owner")
        Page<BookingEntity> findAllWithJoinFetch(Specification<BookingEntity> spec, Pageable pageable);

        List<BookingEntity> findAllByHousingIdAndStatus(
                        Long housingId,
                        RequestStatus status);
}
