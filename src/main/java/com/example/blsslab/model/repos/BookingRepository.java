package com.example.blsslab.model.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.blsslab.model.entity.BookingEntity;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    @Query("select b from BookingEntity b where b.housing.owner.username = :hostUsername")
    List<BookingEntity> findAllByHostName(String hostUsername);
}
