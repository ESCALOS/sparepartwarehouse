package com.nanoka.warehouse.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nanoka.warehouse.Model.Entity.Supplier;
import com.nanoka.warehouse.Service.SupplierService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/supplier")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200"})
public class SupplierController {

    @Autowired
    private SupplierService supplierService;
    
    @GetMapping
    public ResponseEntity<?> getSuppliers()
    {
        return supplierService.getSuppliers();
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<?> getSupplier(@PathVariable Long id)
    {
        return supplierService.getSupplier(id);
    }

    @PostMapping()
    public ResponseEntity<?> saveSupplier(@RequestBody Supplier supplier)
    {
        return supplierService.saveSupplier(supplier);
    }

    @PutMapping()
    public ResponseEntity<?> updateSupplier(@RequestBody Supplier supplier)
    {
        return supplierService.updateSupplier(supplier);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> deleteSupplier(@PathVariable Long id)
    {
        return supplierService.deleteSupplier(id);


    }
}