package com.college.tourism.service;

import com.college.tourism.dto.UserDTO.UserCreateRequestDto;
import com.college.tourism.dto.UserDTO.UserResponseDto;
import com.college.tourism.dto.UserDTO.UserUpdateRequestDto;
import com.college.tourism.dto.helperDTO.CreateBaseResponse;
import com.college.tourism.dto.helperDTO.PaginatedResponse;
import com.college.tourism.dto.helperDTO.ResponseMessage;
import com.college.tourism.dto.helperDTO.UpdateBaseResponse;
import com.college.tourism.entity.User.User;
import com.college.tourism.enums.UserRole;
import com.college.tourism.enums.UserStatus;
import com.college.tourism.mapper.UserMapper;
import com.college.tourism.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;

    // ================= CREATE =================
    public CreateBaseResponse createUser(UserCreateRequestDto dto) {

        if (repository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // 🔐 hash

        if (dto.getRole() != null){
            user.setRole(dto.getRole());
        } else {
            user.setRole(UserRole.USER);
        }
        user.setStatus(UserStatus.ACTIVE);

        User saved = repository.save(user);

        CreateBaseResponse response = new CreateBaseResponse();
        response.setSuccess(true);
        response.setTitle("SUCCESS");
        response.setMessage("User created successfully");
        response.setId(saved.getId());
        response.setCreatedAt(new Date());

        return response;
    }

    // ================= UPDATE =================
    public UpdateBaseResponse updateUser(Long id, UserUpdateRequestDto dto) {

        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (dto.getUserName() != null) user.setUserName(dto.getUserName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getRole() != null) user.setRole(dto.getRole());
        if (dto.getStatus() != null) user.setStatus(dto.getStatus());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        repository.save(user);

        UpdateBaseResponse response = new UpdateBaseResponse();
        response.setSuccess(true);
        response.setTitle("UPDATED");
        response.setMessage("User updated successfully");
        response.setId(user.getId());
        response.setUpdatedAt(new Date());

        return response;
    }

    // ================= GET BY ID =================
    public UserResponseDto getById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapper.toDto(user);
    }

    // ================= GET ALL =================
    @Transactional(readOnly = true)
    public PaginatedResponse<UserResponseDto> getAll(
            int page,
            int size,
            String search,
            UserRole role,
            UserStatus status
    ) {

        List<User> users = repository.findAll();

        // 🔍 1️⃣ Search filter (username OR email)
        if (search != null && !search.isBlank()) {
            String s = search.trim().toLowerCase();
            users = users.stream()
                    .filter(u ->
                            (u.getUserName() != null && u.getUserName().toLowerCase().contains(s)) ||
                                    (u.getEmail() != null && u.getEmail().toLowerCase().contains(s))
                    )
                    .collect(Collectors.toList());
        }

        // 🎭 2️⃣ Role filter
        if (role != null) {
            users = users.stream()
                    .filter(u -> u.getRole() == role)
                    .collect(Collectors.toList());
        }

        // 🚦 3️⃣ Status filter
        if (status != null) {
            users = users.stream()
                    .filter(u -> u.getStatus() == status)
                    .collect(Collectors.toList());
        }

        // ⬇️ 4️⃣ Sort DESC by ID
        users.sort((a, b) -> Long.compare(b.getId(), a.getId()));

        // 📄 5️⃣ Pagination
        int totalElements = users.size();
        page = Math.max(page, 1);

        int start = (page - 1) * size;
        int end = Math.min(start + size, totalElements);

        List<UserResponseDto> data =
                (start < end)
                        ? users.subList(start, end)
                        .stream()
                        .map(mapper::toDto)
                        .collect(Collectors.toList())
                        : List.of();

        return new PaginatedResponse<>(data, page, size, totalElements);
    }

    // ================= DELETE =================
    public ResponseMessage deleteUser(List<Long> ids) {

        if (ids == null || ids.isEmpty()) {
            return new ResponseMessage("No IDs provided for deletion");
        }
        List<User> users = repository.findAllById(ids);
        if (users.isEmpty()) {
            return new ResponseMessage("No tourist places found for given IDs");
        }

        repository.deleteAll(users);
        return new ResponseMessage("User deleted successfully");
    }
}
