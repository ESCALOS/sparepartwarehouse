package com.nanoka.warehouse.Service;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.nanoka.warehouse.Dto.UserDto;
import com.nanoka.warehouse.Jwt.JwtService;
import com.nanoka.warehouse.Model.Entity.User;
import com.nanoka.warehouse.Model.Payload.AuthResponse;
import com.nanoka.warehouse.Model.Payload.MessageResponse;
import com.nanoka.warehouse.Repository.UserRepository;
import com.nanoka.warehouse.Service.Request.LoginRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user=userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token=jwtService.getToken(user);
        return AuthResponse.builder()
            .token(token)
            .build();

    }

    public ResponseEntity<?> currentUser(Principal principal)
    {
        User user = userRepository.findByUsername(principal.getName()).orElse(null);

        UserDto userDto = UserDto.builder()
            .id(user.getId())
            .name(user.getName())
            .username(user.getUsername())
            .role(user.getRole())
            .build();

        MessageResponse response = MessageResponse.builder()
            .message("Usuario encontrado")
            .data(userDto)
            .error(false)
            .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}