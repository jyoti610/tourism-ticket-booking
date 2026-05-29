package com.college.tourism.dto.TouristPlaceDTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class TouristPlaceRequestDto{
   private String name;
   private String city;
   private String description;
   private List<MultipartFile> files;
}