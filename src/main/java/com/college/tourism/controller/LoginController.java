package com.college.tourism.controller;

import com.college.tourism.dto.LoginDTO.*;
import com.college.tourism.dto.helperDTO.ResponseMessage;
import com.college.tourism.security.JwtService;
import com.college.tourism.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LoginController {

    private final AuthService authService;
    private final JwtService jwtService;

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody LoginRequestDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    // LOGOUT
    @PostMapping("/logout")
    public ResponseEntity<ResponseMessage> logout() {
        return ResponseEntity.ok(authService.logout());
    }

    // CHANGE PASSWORD
    @PostMapping("/change-password")
    public ResponseEntity<ResponseMessage> changePassword(
            HttpServletRequest request,
            @RequestBody ChangePasswordDto dto) {

        String email = jwtService.extractEmail(
                jwtService.extractToken(request));

        return ResponseEntity.ok(
                authService.changePassword(email, dto)
        );
    }

    // FORGOT PASSWORD
    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseMessage> forgotPassword(
            @RequestBody ForgotPasswordDto dto) {
        return ResponseEntity.ok(authService.forgotPassword(dto));
    }

    // RESET PASSWORD
    @PostMapping("/reset-password")
    public ResponseEntity<ResponseMessage> resetPassword(
            @RequestBody ResetPasswordDto dto) {
        return ResponseEntity.ok(authService.resetPassword(dto));
    }

}