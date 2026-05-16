package com.example.blsslab.model.postgres.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.blsslab.model.postgres.entity.HousingOutboxEntity;
import com.example.blsslab.model.dto.MessageStatus;
import java.util.List;

@Repository
public interface HousingOutboxRepository extends JpaRepository<HousingOutboxEntity, Long> {
    List<HousingOutboxEntity> findAllByStatus(MessageStatus status);
}
