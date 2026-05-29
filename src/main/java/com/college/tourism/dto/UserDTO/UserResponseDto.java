package com.college.tourism.dto.UserDTO;

import com.college.tourism.enums.UserRole;
import com.college.tourism.enums.UserStatus;
import lombok.Data;

@Data
public class UserResponseDto {

    private Long id;
    private String userName;
    private String email;
    private UserRole role;
    private UserStatus status;
}
