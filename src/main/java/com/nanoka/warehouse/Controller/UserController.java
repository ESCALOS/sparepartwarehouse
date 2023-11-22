package com.nanoka.warehouse.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nanoka.warehouse.Service.UserService;
import com.nanoka.warehouse.Service.Request.UserRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/user")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200"})
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getUsers()
    {
        return userService.getUsers();
    }
    
    @GetMapping(value = "{id}")
    public ResponseEntity<?> getUser(@PathVariable Integer id)
    {
       return userService.getUser(id);
    }

    @PostMapping()
    public ResponseEntity<?> saveUser(@RequestBody UserRequest userRequest)
    {
        return userService.saveUser(userRequest);
    }

    @PutMapping()
    public ResponseEntity<?> updateUser(@RequestBody UserRequest userRequest)
    {
        return userService.updateUser(userRequest);
    }
}