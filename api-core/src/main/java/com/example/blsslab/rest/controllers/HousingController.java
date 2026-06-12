package com.example.blsslab.rest.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.blsslab.model.dto.HousingDTO;
import com.example.blsslab.model.dto.ModerationRequest;
import com.example.blsslab.model.dto.PageInfo;
import com.example.blsslab.service.HousingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/housings")
@RequiredArgsConstructor
public class HousingController {

    final HousingService housingService;

    @GetMapping()
    public PageInfo<HousingDTO> getHousings(@RequestParam(required = false) String search, Pageable pageable) {
        PageInfo<HousingDTO> response = housingService.getAllHousings(pageable, search);
        return response;
    }

    @PostMapping("{id}/moderation")
    public HousingDTO moderateHousing(
            @PathVariable Long id,
            @RequestBody ModerationRequest body) {
        HousingDTO response = housingService.handleRequest(
                // SecurityContextHolder.getContext().getAuthentication().getName(),
                id,
                body.getApproved());
        return response;
    }

    @PostMapping()
    public HousingDTO addHousing(@RequestBody HousingDTO housing) {
        HousingDTO response = housingService.addHousing(housing);
        return response;
    }

    @PatchMapping("/{id}")
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
