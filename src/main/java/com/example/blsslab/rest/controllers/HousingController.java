package com.example.blsslab.rest.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.blsslab.model.dto.HousingDTO;
import com.example.blsslab.model.dto.ModerationRequest;
import com.example.blsslab.model.dto.ResponseDTO;
import com.example.blsslab.service.HousingService;

// TODO: Редактирование, удаление
@RestController
@RequestMapping("/housings")
public class HousingController {

    HousingService housingService;

    public HousingController(HousingService housingService) {
        this.housingService = housingService;
    }

    // TODO: Фильтрация, Пейджинг, Сортировка
    @GetMapping()
    public List<HousingDTO> getHousings(@RequestParam(required = false) String username) {
        ResponseDTO<List<HousingDTO>> response = housingService.getAllHousings();
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
    public ResponseDTO<HousingDTO> addHousing(
            @RequestBody HousingDTO housing) {
        ResponseDTO<HousingDTO> response = housingService.addHousing(housing);
        return response;
    }
}
