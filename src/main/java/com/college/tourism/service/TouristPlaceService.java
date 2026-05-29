package com.college.tourism.service;

import com.college.tourism.dto.ImgBBDTO.ImgBBResponse;
import com.college.tourism.dto.TouristPlaceDTO.TouristPlaceRequestDto;
import com.college.tourism.dto.TouristPlaceDTO.TouristPlaceResponseDto;
import com.college.tourism.dto.helperDTO.CreateBaseResponse;
import com.college.tourism.dto.helperDTO.PaginatedResponse;
import com.college.tourism.dto.helperDTO.ResponseMessage;
import com.college.tourism.dto.helperDTO.UpdateBaseResponse;
import com.college.tourism.entity.TouristPlace.TouristPlace;
import com.college.tourism.mapper.TouristPlaceMapper;
import com.college.tourism.repository.TouristPlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TouristPlaceService {

    private final TouristPlaceRepository repository;
    private final TouristPlaceMapper mapper;

    @Value("${imgbb.api.key}")
    private String imgbbApiKey;

    private static final String IMGBB_URL = "https://api.imgbb.com/1/upload";

    // ================= CREATE =================
    public CreateBaseResponse addPlace(TouristPlaceRequestDto dto) {

        List<String> imageUrls = uploadImages(dto.getFiles());

        TouristPlace place = new TouristPlace();
        place.setName(dto.getName());
        place.setCity(dto.getCity());
        place.setDescription(dto.getDescription());
        place.setImageUrl(imageUrls);

        TouristPlace saved = repository.save(place);

        CreateBaseResponse response = new CreateBaseResponse();
        response.setSuccess(true);
        response.setTitle("SUCCESS");
        response.setMessage("Tourist place created successfully");
        response.setId(saved.getId());
        response.setCreatedAt(new Date());

        return response;
    }

    // ================= UPDATE =================
    public UpdateBaseResponse updatePlace(Long id, TouristPlaceRequestDto dto) {

        TouristPlace place = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Tourist place not found"));

        if (dto.getName() != null) place.setName(dto.getName());
        if (dto.getCity() != null) place.setCity(dto.getCity());
        if (dto.getDescription() != null) place.setDescription(dto.getDescription());

        if (dto.getFiles() != null && !dto.getFiles().isEmpty()) {
            List<String> imageUrls = uploadImages(dto.getFiles());
            place.setImageUrl(imageUrls);
        }

        TouristPlace updated = repository.save(place);

        UpdateBaseResponse response = new UpdateBaseResponse();
        response.setSuccess(true);
        response.setTitle("UPDATED");
        response.setMessage("Tourist place updated successfully");
        response.setId(updated.getId());
        response.setUpdatedAt(new Date());

        return response;
    }

    // ================= GET BY ID =================
    public TouristPlaceResponseDto getById(Long id) {
        TouristPlace place = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Tourist place not found"));
        return mapper.toDto(place);
    }

    // ================= GET ALL =================
    @Transactional(readOnly = true)
    public PaginatedResponse<TouristPlaceResponseDto> getAll(
            int page,
            int size,
            String search
    ) {

        List<TouristPlace> places = repository.findAll();

        // 1️⃣ Search filter
        if (search != null && !search.isBlank()) {
            String s = search.trim().toLowerCase();
            places = places.stream()
                    .filter(p ->
                            p.getName() != null &&
                                    p.getName().toLowerCase().contains(s)
                    )
                    .collect(Collectors.toList());
        }

        // 2️⃣ Sort DESC
        places.sort((a, b) -> Long.compare(b.getId(), a.getId()));

        // 3️⃣ Pagination
        int totalElements = places.size();
        page = Math.max(page, 1);

        int start = (page - 1) * size;
        int end = Math.min(start + size, totalElements);

        List<TouristPlaceResponseDto> data =
                (start < end)
                        ? places.subList(start, end)
                        .stream()
                        .map(mapper::toDto)
                        .collect(Collectors.toList())
                        : List.of();

        return new PaginatedResponse<>(data, page, size, totalElements);
    }


    // ================= DELETE MULTIPLE =================
    public ResponseMessage deleteMultiple(List<Long> ids) {

        if (ids == null || ids.isEmpty()) {
            return new ResponseMessage("No IDs provided for deletion");
        }

        List<TouristPlace> places = repository.findAllById(ids);

        if (places.isEmpty()) {
            return new ResponseMessage("No tourist places found for given IDs");
        }

        repository.deleteAll(places);

        return new ResponseMessage("Tourist places deleted successfully");
    }

    // ================= IMAGE HELPERS =================
    private List<String> uploadImages(List<MultipartFile> files) {

        List<String> imageUrls = new ArrayList<>();

        if (files != null) {
            for (MultipartFile file : files) {
                String url = uploadToImgBB(file);
                if (url != null) imageUrls.add(url);
            }
        }
        return imageUrls;
    }

    private String uploadToImgBB(MultipartFile file) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("key", imgbbApiKey);
            body.add("image", file.getResource());

            HttpEntity<MultiValueMap<String, Object>> request =
                    new HttpEntity<>(body, headers);

            ResponseEntity<ImgBBResponse> response =
                    restTemplate.exchange(
                            IMGBB_URL,
                            HttpMethod.POST,
                            request,
                            ImgBBResponse.class
                    );

            if (response.getStatusCode() == HttpStatus.OK &&
                    response.getBody() != null &&
                    response.getBody().getData() != null) {
                return response.getBody().getData().getUrl();
            }

        } catch (Exception e) {
            log.error("ImgBB upload failed", e);
        }
        return null;
    }
}
