package com.example.blsslab.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.blsslab.model.entity.ReceivedMessageEntity;
import java.util.List;
import com.example.blsslab.model.dto.RequestStatus;

@Repository
public interface ReceivedMessageRepository extends JpaRepository<ReceivedMessageEntity, Long> {
    List<ReceivedMessageEntity> findByStatus(RequestStatus status);
}
