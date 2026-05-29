package com.college.tourism.dto.UserDTO;

import com.college.tourism.enums.UserRole;
import lombok.Data;

@Data
public class UserCreateRequestDto {

    private String userName;
    private String email;
    private String password; // plain password (will be hashed)
    private UserRole role = UserRole.USER;
}
