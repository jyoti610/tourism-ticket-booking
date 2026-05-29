package com.college.tourism.dto.UserDTO;

import com.college.tourism.enums.UserRole;
import com.college.tourism.enums.UserStatus;
import lombok.Data;

@Data
public class UserUpdateRequestDto {

    private String userName;
    private String email;
    private String password; // optional
    private UserRole role;
    private UserStatus status;
}