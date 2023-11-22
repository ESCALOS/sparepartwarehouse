package com.nanoka.warehouse.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nanoka.warehouse.Service.SupplierService;
import com.nanoka.warehouse.Service.Request.SupplierRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/supplier")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200"})
public class SupplierController {

    private final SupplierService supplierService;
    
    @GetMapping(value = "{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id)
    {
        return supplierService.findById(id);
    }

    @PostMapping()
    public ResponseEntity<?> saveUser(@RequestBody SupplierRequest supplierRequest)
    {
        return supplierService.save(supplierRequest);
    }

    @PutMapping()
    public ResponseEntity<?> updateUser(@RequestBody SupplierRequest supplierRequest)
    {
        return supplierService.update(supplierRequest);
    }
}