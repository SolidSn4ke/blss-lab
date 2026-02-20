package com.example.blsslab.rest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blsslab.model.dto.HousingDTO;
import com.example.blsslab.model.dto.ResponseDTO;
import com.example.blsslab.service.HousingService;

@RestController
@RequestMapping("/housing")
public class HousingController {

    @Autowired
    HousingService housingService;

    @GetMapping("/all")
    public ResponseEntity<List<HousingDTO>> getHousings() {
        ResponseDTO<List<HousingDTO>> response = housingService.getAllHousings();
        return new ResponseEntity<List<HousingDTO>>(response.getEntity(), HttpStatusCode.valueOf(response.getCode()));
    }

    @GetMapping("/{username}/all-to-handle")
    public ResponseEntity<List<HousingDTO>> getMethodName(@PathVariable String username) {
        ResponseDTO<List<HousingDTO>> response = housingService.getAllHousingsToHandle(username);
        return new ResponseEntity<List<HousingDTO>>(response.getEntity(), HttpStatusCode.valueOf(response.getCode()));
    }

    @PostMapping("/{username}/add-housing")
    public ResponseEntity<ResponseDTO<HousingDTO>> addHousing(
            @PathVariable String username,
            @RequestBody HousingDTO housing) {
        ResponseDTO<HousingDTO> response = housingService.addHousing(username, housing);
        return new ResponseEntity<ResponseDTO<HousingDTO>>(response, HttpStatusCode.valueOf(response.getCode()));
    }

}
