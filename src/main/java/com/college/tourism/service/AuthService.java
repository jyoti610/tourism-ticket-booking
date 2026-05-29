package com.college.tourism.service;

import com.college.tourism.dto.LoginDTO.*;
import com.college.tourism.dto.helperDTO.ResponseMessage;
import com.college.tourism.entity.User.User;
import com.college.tourism.repository.UserRepository;
import com.college.tourism.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    // TEMP OTP STORE (use Redis / DB in real apps)
    private final Map<String, String> otpStore = new HashMap<>();

    // ================= LOGIN =================
    public LoginResponseDto login(LoginRequestDto dto) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getPassword()
                )
        );

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

        String token = jwtService.generateToken(
                userDetails,
                user.getId(),
                user.getRole().name()
        );
        Map<String, String> userData = new HashMap<>();
        userData.put("userName", user.getUserName());
        userData.put("phone", "0987654321");

        return new LoginResponseDto(token, "Login successful",userData);
    }

    // ================= LOGOUT =================
    public ResponseMessage logout() {
        return new ResponseMessage("Logout successful");
    }

    // ================= CHANGE PASSWORD =================
    public ResponseMessage changePassword(String email, ChangePasswordDto dto) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        return new ResponseMessage("Password changed successfully");
    }

    // ================= FORGOT PASSWORD =================
    public ResponseMessage forgotPassword(ForgotPasswordDto dto) {

        userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        otpStore.put(dto.getEmail(), otp);

        // MOCK EMAIL
        System.out.println("🔐 OTP for " + dto.getEmail() + " = " + otp);

        return new ResponseMessage("OTP sent to registered email");
    }

    // ================= RESET PASSWORD =================
    public ResponseMessage resetPassword(ResetPasswordDto dto) {

        String storedOtp = otpStore.get(dto.getEmail());

        if (storedOtp == null || !storedOtp.equals(dto.getOtp())) {
            throw new RuntimeException("Invalid or expired OTP");
        }

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        otpStore.remove(dto.getEmail());

        return new ResponseMessage("Password reset successful");
    }
}
