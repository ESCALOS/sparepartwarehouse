package com.nanoka.warehouse.Service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nanoka.warehouse.Dto.SupplierDto;
import com.nanoka.warehouse.Model.Entity.Supplier;
import com.nanoka.warehouse.Model.Payload.MessageResponse;
import com.nanoka.warehouse.Repository.SupplierRepository;
import com.nanoka.warehouse.Service.Request.SupplierRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplierService {
    
    private final SupplierRepository supplierRepository;

    public ResponseEntity<?> findById(Long id) 
    {
        Supplier supplier = supplierRepository.findById(id).orElse(null);
        if(supplier != null)
        {
            SupplierDto supplierDto = SupplierDto.builder()
                .id(id)
                .name(supplier.getName())
                .address(supplier.getAddress())
                .telephone(supplier.getTelephone())
                .email(supplier.getEmail())
                .build();

            MessageResponse response = MessageResponse.builder()
                .message("Proveedor encontrado")
                .error(false)
                .data(supplierDto)
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
    public ResponseEntity<?> save(SupplierRequest supplierRequest)
    {
        MessageResponse response;
        HttpStatus status;

        if(supplierRepository.existsByName(supplierRequest.getName())) {
            response = MessageResponse.builder()
               .message("El proveedor ya existe")
               .error(true)
               .data(null)
               .build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Supplier supplier = Supplier.builder()
          .name(supplierRequest.getName())
          .address(supplierRequest.getAddress())
          .telephone(supplierRequest.getTelephone())
          .email(supplierRequest.getEmail())
          .build();

        Supplier supplierSaved = supplierRepository.save(supplier);

        if(supplierSaved != null)
        {
            SupplierDto supplierDto = SupplierDto.builder()
               .id(supplierSaved.getId())
               .name(supplierSaved.getName())
               .address(supplierSaved.getAddress())
               .telephone(supplierSaved.getTelephone())
               .email(supplierSaved.getEmail())
               .build();
            status = HttpStatus.CREATED;
            response = MessageResponse.builder()
                .message("Proveedor creado")
                .error(false)
                .data(supplierDto)
                .build();
                
        }else{
            status = HttpStatus.BAD_REQUEST;
            response = MessageResponse.builder()
               .message("No se pudo crear el proveedor")
               .error(true)
               .data(null)
               .build();
        }

        return new ResponseEntity<>(response, status);
    }

    public ResponseEntity<?> update(SupplierRequest supplierRequest)
    {
        MessageResponse response;
        HttpStatus status;

        if(supplierRepository.countSuppliersWithSameNameExceptCurrentUser(supplierRequest.getName(), supplierRequest.getId()) > 0) {
            status = HttpStatus.BAD_REQUEST;
            response = MessageResponse.builder()
               .message("El proveedor ya existe")
               .error(true)
               .data(null)
               .build();
        }else{
            Supplier supplier = Supplier.builder()
                .id(supplierRequest.getId())
                .name(supplierRequest.getName())
                .address(supplierRequest.getAddress())
                .telephone(supplierRequest.getTelephone())
                .email(supplierRequest.getEmail())
                .build();

                Supplier supplierSaved = supplierRepository.save(supplier);

                if(supplierSaved != null)
                {
                    SupplierDto supplierDto = SupplierDto.builder()
                    .id(supplierSaved.getId())
                    .name(supplierSaved.getName())
                    .address(supplierSaved.getAddress())
                    .telephone(supplierSaved.getTelephone())
                    .email(supplierSaved.getEmail())
                    .build();
                    status = HttpStatus.CREATED;
                    response = MessageResponse.builder()
                        .message("Proveedor actualizado")
                        .error(false)
                        .data(supplierDto)
                        .build();
                        
                }else{
                    status = HttpStatus.BAD_REQUEST;
                    response = MessageResponse.builder()
                    .message("No se pudo crear el proveedor")
                    .error(true)
                    .data(null)
                    .build();
                }
        }
        return new ResponseEntity<>(response, status);
    }

}
