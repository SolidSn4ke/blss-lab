package com.example.blsslab.rest.controllers;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.blsslab.model.dto.HousingDTO;
import com.example.blsslab.model.dto.ModerationRequest;
import com.example.blsslab.model.dto.ResponseDTO;
import com.example.blsslab.service.HousingService;

@RestController
@RequestMapping("/housings")
public class HousingController {

    HousingService housingService;

    public HousingController(HousingService housingService) {
        this.housingService = housingService;
    }

    @GetMapping()
    public List<HousingDTO> getHousings(@RequestParam(required = false) String username,
            @RequestParam(required = false) String search, Pageable pageable) {
        ResponseDTO<List<HousingDTO>> response = housingService.getAllHousings(pageable, search);
        return response.getEntity();
    }

    @PostMapping("{id}/moderation")
    public ResponseDTO<HousingDTO> moderateHousing(
            @PathVariable Long id,
            @RequestBody ModerationRequest body) {
        ResponseDTO<HousingDTO> response = housingService.handleRequest(body.getUser().getUsername(), id,
                body.getApproved());
        return response;
    }

    @PostMapping()
    public ResponseDTO<HousingDTO> addHousing(@RequestBody HousingDTO housing) {
        ResponseDTO<HousingDTO> response = housingService.addHousing(housing);
        return response;
    }

    @PutMapping("/{id}")
    public HousingDTO updateHousing(@PathVariable Long id, @RequestBody HousingDTO entity) {
        HousingDTO response = housingService.updateHousing(id, entity);
        return response;
    }

    @DeleteMapping("/{id}")
    public Boolean deleteHousing(@PathVariable Long id) {
        Boolean response = housingService.deleteHousing(id);
        return response;
    }
}
