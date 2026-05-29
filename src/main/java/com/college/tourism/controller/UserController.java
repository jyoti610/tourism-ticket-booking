package com.college.tourism.controller;

import com.college.tourism.dto.UserDTO.UserCreateRequestDto;
import com.college.tourism.dto.UserDTO.UserResponseDto;
import com.college.tourism.dto.UserDTO.UserUpdateRequestDto;
import com.college.tourism.dto.helperDTO.CreateBaseResponse;
import com.college.tourism.dto.helperDTO.PaginatedResponse;
import com.college.tourism.dto.helperDTO.ResponseMessage;
import com.college.tourism.dto.helperDTO.UpdateBaseResponse;
import com.college.tourism.enums.UserRole;
import com.college.tourism.enums.UserStatus;
import com.college.tourism.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService service;

    @PostMapping("/createUser")
    public ResponseEntity<CreateBaseResponse> create(
            @RequestBody UserCreateRequestDto dto) {
        return ResponseEntity.ok(service.createUser(dto));
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<UpdateBaseResponse> update(
            @PathVariable Long id,
            @RequestBody UserUpdateRequestDto dto) {
        return ResponseEntity.ok(service.updateUser(id, dto));
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/getAllUser")
    public ResponseEntity<PaginatedResponse<UserResponseDto>> getAllUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) UserRole role,
            @RequestParam(required = false) UserStatus status
    ) {
        return ResponseEntity.ok(
                service.getAll(page, size, search, role, status)
        );
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<ResponseMessage> delete(
            @RequestBody List<Long> ids) {
        return ResponseEntity.ok(service.deleteUser(ids));
    }
}
