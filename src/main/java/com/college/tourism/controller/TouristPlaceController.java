package com.college.tourism.controller;

import com.college.tourism.dto.TouristPlaceDTO.TouristPlaceRequestDto;
import com.college.tourism.dto.TouristPlaceDTO.TouristPlaceResponseDto;
import com.college.tourism.dto.helperDTO.CreateBaseResponse;
import com.college.tourism.dto.helperDTO.PaginatedResponse;
import com.college.tourism.dto.helperDTO.ResponseMessage;
import com.college.tourism.dto.helperDTO.UpdateBaseResponse;
import com.college.tourism.service.TouristPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TouristPlaceController {

    private final TouristPlaceService service;

    // CREATE
    @PostMapping(value = "/createPlaces", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CreateBaseResponse> createPlace(
            @RequestParam String name,
            @RequestParam String city,
            @RequestParam String description,
            @RequestParam(required = false) List<MultipartFile> files) {

        TouristPlaceRequestDto dto = new TouristPlaceRequestDto();
        dto.setName(name);
        dto.setCity(city);
        dto.setDescription(description);
        dto.setFiles(files);

        return ResponseEntity.ok(service.addPlace(dto));
    }

    // UPDATE
    @PutMapping(value = "/updatePlaces/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UpdateBaseResponse> updatePlace(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) List<MultipartFile> files) {

        TouristPlaceRequestDto dto = new TouristPlaceRequestDto();
        dto.setName(name);
        dto.setCity(city);
        dto.setDescription(description);
        dto.setFiles(files);

        return ResponseEntity.ok(service.updatePlace(id, dto));
    }

    // GET BY ID
    @GetMapping("/getPlace/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // GET ALL
    @GetMapping("/getAllPlaces")
    public ResponseEntity<PaginatedResponse<TouristPlaceResponseDto>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search
    ) {
        return ResponseEntity.ok(service.getAll(page, size, search));
    }


    // DELETE MULTIPLE
    @DeleteMapping("/deletePlaces")
    public ResponseEntity<ResponseMessage> deleteMultiple(
            @RequestBody List<Long> ids) {
        return ResponseEntity.ok(service.deleteMultiple(ids));
    }
}
