package com.example.blsslab.model.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.blsslab.model.entity.BookingEntity;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

}
