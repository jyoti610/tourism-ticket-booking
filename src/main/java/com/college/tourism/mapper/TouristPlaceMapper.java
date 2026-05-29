package com.college.tourism.mapper;

import com.college.tourism.dto.TouristPlaceDTO.TouristPlaceResponseDto;
import com.college.tourism.entity.TouristPlace.TouristPlace;
import org.springframework.stereotype.Component;

@Component
public class TouristPlaceMapper {

    public TouristPlaceResponseDto toDto(TouristPlace entity) {
        TouristPlaceResponseDto dto = new TouristPlaceResponseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCity(entity.getCity());
        dto.setDescription(entity.getDescription());
        dto.setImageUrl(entity.getImageUrl());
        return dto;
    }
}
