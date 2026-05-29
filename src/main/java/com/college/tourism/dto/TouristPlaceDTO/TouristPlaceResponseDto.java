package com.college.tourism.dto.TouristPlaceDTO;

import lombok.Data;

import java.util.List;

@Data
public class TouristPlaceResponseDto {

    private Long id;
    private String name;
    private String city;
    private String description;
    private List<String> imageUrl;
}
