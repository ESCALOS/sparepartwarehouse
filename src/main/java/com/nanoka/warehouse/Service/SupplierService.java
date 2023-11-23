package com.nanoka.warehouse.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nanoka.warehouse.Model.Entity.Supplier;
import com.nanoka.warehouse.Model.Payload.MessageResponse;
import com.nanoka.warehouse.Repository.SupplierRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplierService {
    
    @Autowired
    private SupplierRepository supplierRepository;

    public ResponseEntity<?> getSuppliers()
    {
        List<Supplier> users = supplierRepository.findAll();

        MessageResponse response = MessageResponse.builder()
            .message("Lista de proveedores")
            .error(false)
            .data(users)
            .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> getSupplier(Long id) 
    {
        Supplier supplier = supplierRepository.findById(id).orElse(null);
        if(supplier != null)
        {
            MessageResponse response = MessageResponse.builder()
                .message("Proveedor encontrado")
                .error(false)
                .data(supplier)
                .build();
            return new ResponseEntity<>(response,HttpStatus.OK);
        }

        MessageResponse response = MessageResponse.builder()
           .message("Proveedor no encontrado")
           .error(true)
           .data(null)
           .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @Transactional
    public ResponseEntity<?> saveSupplier(Supplier supplier)
    {
        if(supplierRepository.existsByName(supplier.getName())) {
            MessageResponse response = MessageResponse.builder()
               .message("El proveedor ya existe")
               .error(true)
               .data(null)
               .build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            supplierRepository.save(supplier);
            MessageResponse response = MessageResponse.builder()
                .message("Proveedor creado")
                .error(false)
                .data(supplier)
                .build();
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            MessageResponse response = MessageResponse.builder()
               .message("No se pudo crear el proveedor")
               .error(true)
               .data(null)
               .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> updateSupplier(Supplier supplier)
    {
        if(supplierRepository.countSuppliersWithSameNameExceptCurrentSupplier(supplier.getName(), supplier.getId()) > 0) {
            MessageResponse response = MessageResponse.builder()
               .message("El proveedor ya existe")
               .error(true)
               .data(null)
               .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            supplierRepository.save(supplier);
            MessageResponse response = MessageResponse.builder()
                .message("Proveedor actualizado")
                .error(false)
                .data(supplier)
                .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            MessageResponse response = MessageResponse.builder()
            .message("No se pudo crear el proveedor")
            .error(true)
            .data(null)
            .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> deleteSupplier(Long id)
    {
        if (!supplierRepository.existsById(id)) {
            MessageResponse response = MessageResponse.builder()
                .message("El proveedor no existe")
                .error(true)
                .data(null)
                .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            supplierRepository.deleteById(id);
            MessageResponse response = MessageResponse.builder()
                .message("Proveedor eliminado")
                .error(false)
                .data(null)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            MessageResponse response = MessageResponse.builder()
                .message("No se pudo eliminar")
                .error(true)
                .data(null)
                .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
