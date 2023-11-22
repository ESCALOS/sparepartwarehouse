package com.nanoka.warehouse.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nanoka.warehouse.Dto.UserDto;
import com.nanoka.warehouse.Model.Entity.User;
import com.nanoka.warehouse.Model.Enum.Role;
import com.nanoka.warehouse.Model.Payload.MessageResponse;
import com.nanoka.warehouse.Repository.UserRepository;
import com.nanoka.warehouse.Service.Request.UserRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<?> getUsers()
    {
        List<User> users = userRepository.findAll();

        List<UserDto> usersDto = users.stream()
                                    .map(this::userToDto)
                                    .collect(Collectors.toList());

        MessageResponse response = MessageResponse.builder()
            .message("Lista de usuarios")
            .error(false)
            .data(usersDto)
            .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
                                    
    }

    private UserDto userToDto(User user)
    {
        return modelMapper.map(user, UserDto.class);
    }

    @Transactional
    public ResponseEntity<?> saveUser(UserRequest userRequest) {

        if(userRepository.existsByUsername(userRequest.getUsername())) {
            MessageResponse response = MessageResponse.builder()
                .message("El nombre de usuario ya se encuentra en uso")
                .error(true)
                .data(null)
                .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        User user = User.builder()
            .username(userRequest.getUsername())
            .password(passwordEncoder.encode( userRequest.getPassword()))
            .name(userRequest.getName())
            .role(Role.USER)
            .build();

        User userSaved = userRepository.save(user);

        MessageResponse response;
        HttpStatus status;

        if(userSaved != null)
        {
            UserDto userDto = UserDto.builder()
                .id(userSaved.getId())
                .name(userSaved.getName())
                .username(userSaved.getUsername())
                .role(userSaved.getRole())
                .build();

            status = HttpStatus.CREATED;
            response = MessageResponse.builder()
                .message("El usuario se registró satisfactoriamente")
                .error(false)
                .data(userDto)
                .build();
        }else{
            status = HttpStatus.BAD_REQUEST;
            response = MessageResponse.builder()
                .message("No se pudo crear al usuario")
                .error(true)
                .data(null)
                .build();
        }
        
        return new ResponseEntity<>(response, status);
    }

    @Transactional
    public ResponseEntity<?> updateUser(UserRequest userRequest) {
        MessageResponse response;
        HttpStatus status;

        if(userRepository.existsById(userRequest.getId())) {

            userRepository.updateUser(userRequest.getId(), userRequest.getName());

            User userUpdated = userRepository.findById(userRequest.getId()).orElse(null);

            UserDto userDto = UserDto.builder()
                .id(userUpdated.getId())
                .name(userUpdated.getName())
                .username(userUpdated.getUsername())
                .role(userUpdated.getRole())
                .build();

            status = HttpStatus.OK;
            response = MessageResponse.builder()
                .message("Usuario actualizado")
                .error(false)
                .data(userDto)
                .build();
        }else{
            status = HttpStatus.BAD_REQUEST;
            response = MessageResponse.builder()
                .message("El usuario no existe")
                .error(true)
                .data(null)
                .build();
        }

        return new ResponseEntity<>(response, status);
    }

    public ResponseEntity<?> getUser(Integer id) {
        User user = userRepository.findById(id).orElse(null);
       
        MessageResponse response;
        HttpStatus status;

        if (user!=null)
        {
            UserDto userDto = UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .role(user.getRole())
                .build();

            status = HttpStatus.OK;
            response = MessageResponse.builder()
                .message("Usuario encontrado")
                .error(false)
                .data(userDto)
                .build();
        }else{
            status = HttpStatus.BAD_REQUEST;
            response = MessageResponse.builder()
                .message("El usuario no existe")
                .error(true)
                .data(null)
                .build();
        }
        
        return new ResponseEntity<>(response, status);
    }
}