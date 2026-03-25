package com.example.blsslab.model.db1.entity;

import com.example.blsslab.model.dto.HousingDTO;
import com.example.blsslab.model.dto.HousingType;
import com.example.blsslab.model.dto.RequestStatus;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "housing")
public class HousingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(0)
    @NotNull
    private Long price;

    @Min(0)
    @NotNull
    private Integer numOfBeds;

    @Min(0)
    @Max(5)
    private Double rating;

    @Version
    private Long version;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private HousingType housingType;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    @Column(name = "owner")
    @NotBlank
    private String owner;

    public void update(HousingDTO housingDTO) {
        if (housingDTO.getPrice() != null) {
            this.price = housingDTO.getPrice();
        }
        if (housingDTO.getNumOfBeds() != null) {
            this.numOfBeds = housingDTO.getNumOfBeds();
        }
        if (housingDTO.getRating() != null) {
            this.rating = housingDTO.getRating();
        }
        if (housingDTO.getHousingType() != null) {
            this.housingType = housingDTO.getHousingType();
        }
    }
}
