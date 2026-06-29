package com.skcodify.myshop.controllers;

import com.skcodify.myshop.domain.Society;
import com.skcodify.myshop.dto.CreateSocietyRequest;
import com.skcodify.myshop.dto.SocietyDto;
import com.skcodify.myshop.service.SocietyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
// CORS is configured globally in WebConfig.java
public class SocietyController {

    private final SocietyService societyService;

    public SocietyController(SocietyService societyService) {
        this.societyService = societyService;
    }

    @PostMapping("/admin/societies")
    public ResponseEntity<SocietyDto> createSociety(@RequestBody CreateSocietyRequest request) {
        Society newSociety = new Society();
        newSociety.setName(request.getName());
        newSociety.setArea(request.getArea());

        Society createdSociety = societyService.createSociety(newSociety);

        SocietyDto dto = new SocietyDto();
        dto.setId(createdSociety.getId());
        dto.setName(createdSociety.getName());
        dto.setArea(createdSociety.getArea());

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/societies")
    public ResponseEntity<List<SocietyDto>> getAllSocieties() {
        List<Society> societies = societyService.getAllSocieties();
        List<SocietyDto> dtoList = societies.stream().map(s -> {
            SocietyDto dto = new SocietyDto();
            dto.setId(s.getId());
            dto.setName(s.getName());
            dto.setArea(s.getArea());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }
}
