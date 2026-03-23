package com.example.blsslab.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.example.blsslab.exception.AlreadyProcessedException;
import com.example.blsslab.exception.BadRequestBodyException;
import com.example.blsslab.exception.DependencyViolationException;
import com.example.blsslab.exception.RolePrivilegesViolationException;
import com.example.blsslab.model.dto.HousingDTO;
import com.example.blsslab.model.dto.PageInfo;
import com.example.blsslab.model.dto.RequestStatus;
import com.example.blsslab.model.dto.UserRole;
import com.example.blsslab.model.entity.AddressEntity;
import com.example.blsslab.model.entity.BookingEntity;
import com.example.blsslab.model.entity.HousingEntity;
import com.example.blsslab.model.entity.UserEntity;
import com.example.blsslab.model.repos.AddressRepository;
import com.example.blsslab.model.repos.BookingRepository;
import com.example.blsslab.model.repos.HousingRepository;
import com.example.blsslab.model.repos.UserRepository;
import com.example.blsslab.specs.CustomSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HousingService {

    final HousingRepository housingRepo;

    final UserRepository userRepo;

    final AddressRepository addressRepo;

    final BookingRepository bookingRepo;

    @Transactional(readOnly = true)
    public PageInfo<HousingDTO> getAllHousings(Pageable pageable, String searchQuery) {
        Page<HousingEntity> result = housingRepo.findAllWithJoinFetch(CustomSpecification.buildFromFilters(searchQuery),
                pageable);
        List<HousingDTO> content = result.toList().stream().map(h -> new HousingDTO(h)).toList();
        return new PageInfo<HousingDTO>(content, result.getTotalPages(), result.getNumber(), result.getTotalElements());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public HousingDTO handleRequest(String username, Long id, Boolean approved) {
        UserEntity user = userRepo.findById(username)
                .orElseThrow(() -> new EntityNotFoundException("Failed to retrieve user by username"));

        if (user.getRole() != UserRole.MODERATOR) {
            throw new RolePrivilegesViolationException("Only moderator has access to this action");
        }

        if (approved == null) {
            throw new BadRequestBodyException("Field 'approved' is required");
        }

        HousingEntity housing = housingRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Failed to retrieve housing by id"));

        if (housing.getStatus() != RequestStatus.PENDING) {
            throw new AlreadyProcessedException("Housing request already processed");
        }

        if (approved) {
            housing.setStatus(RequestStatus.CONFIRMED);
        } else {
            housing.setStatus(RequestStatus.CANCELLED);
        }

        housingRepo.save(housing);

        return new HousingDTO(housing);
    }

    @Transactional
    public HousingDTO addHousing(HousingDTO housing) {
        UserEntity owner = userRepo.findById(housing.getOwner().getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Failed to retrieve user by username"));

        HousingEntity housingEntity = new HousingEntity();
        housingEntity.setPrice(housing.getPrice());
        housingEntity.setNumOfBeds(housing.getNumOfBeds());
        housingEntity.setRating(housing.getRating());
        housingEntity.setHousingType(housing.getHousingType());
        housingEntity.setStatus(RequestStatus.PENDING);
        housingEntity.setOwner(owner);

        AddressEntity address;

        if (housing.getAddress().getId() == null) {
            address = new AddressEntity();
            address.setStreet(housing.getAddress().getStreet());
            address.setCountry(housing.getAddress().getCountry());
            addressRepo.save(address);
        } else {
            address = addressRepo.findById(housing.getAddress().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Failed to retrieve address by id"));
        }

        housingEntity.setAddress(address);

        housingRepo.save(housingEntity);

        return new HousingDTO(housingEntity);
    }

    @Transactional
    public HousingDTO updateHousing(Long id, HousingDTO housingDTO) {
        HousingEntity existHousing = housingRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Failed to retrieve housing by id"));

        List<BookingEntity> confirmedBookings = bookingRepo.findAllByHousingIdAndStatus(id, RequestStatus.CONFIRMED);
        if (confirmedBookings.stream()
                .filter(booking -> booking.getCheckOut().isAfter(LocalDate.now()))
                .count() > 0) {
            throw new DependencyViolationException("There are already confirmed bookings for this housing");
        }

        existHousing.update(housingDTO);

        if (housingDTO.getOwner() != null) {
            UserEntity owner = userRepo.findById(housingDTO.getOwner().getUsername())
                    .orElseThrow(() -> new EntityNotFoundException("Failed to retrieve user by username"));
            existHousing.setOwner(owner);
        }

        if (housingDTO.getAddress() != null) {
            AddressEntity address;
            if (housingDTO.getAddress().getId() == null) {
                address = new AddressEntity();
                address.setStreet(housingDTO.getAddress().getStreet());
                address.setCountry(housingDTO.getAddress().getCountry());
                addressRepo.save(address);
            } else {
                address = addressRepo.findById(housingDTO.getAddress().getId())
                        .orElseThrow(() -> new EntityNotFoundException("Failed to retrieve address by id"));
            }
            existHousing.setAddress(address);
        }

        existHousing.setStatus(RequestStatus.PENDING);
        housingRepo.save(existHousing);
        return new HousingDTO(existHousing);
    }

    @Transactional
    public Boolean deleteHousing(Long id) {
        housingRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Failed to retrieve housing by id"));

        List<BookingEntity> confirmedBookings = bookingRepo.findAllByHousingIdAndStatus(id, RequestStatus.CONFIRMED);
        if (confirmedBookings.stream()
                .filter(booking -> booking.getCheckOut().isAfter(LocalDate.now()))
                .count() > 0) {
            throw new DependencyViolationException("There are already confirmed bookings for this housing");
        }

        housingRepo.deleteById(id);
        return true;
    }
}
