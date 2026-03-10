package com.example.blsslab.model.repos;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.blsslab.model.dto.RequestStatus;
import com.example.blsslab.model.entity.BookingEntity;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    Page<BookingEntity> findAllByGuestUsername(
            String username,
            Pageable pageable);

    Page<BookingEntity> findAllByHousingOwnerUsername(
            String username,
            Pageable pageable);

    Page<BookingEntity> findAllByGuestUsernameOrHousingOwnerUsername(
            String guestUsername,
            String ownerUsername,
            Pageable pageable);

    List<BookingEntity> findAllByHousingIdAndStatus(
            Long housingId,
            RequestStatus status);
}
