package com.nanoka.warehouse.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nanoka.warehouse.Model.Entity.Movement;
import com.nanoka.warehouse.Model.Entity.Product;
import com.nanoka.warehouse.Model.Enum.MovementType;
import com.nanoka.warehouse.Model.Payload.MessageResponse;
import com.nanoka.warehouse.Repository.MovementRepository;
import com.nanoka.warehouse.Repository.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovementService {
    @Autowired
    private MovementRepository movementRepository;

    @Autowired ProductRepository productRepository;

    public ResponseEntity<?> getMovements()
    {
        List<Movement> movements = movementRepository.findAll();

        MessageResponse response = MessageResponse.builder()
            .message("Lista de movimientos")
            .error(false)
            .data(movements)
            .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> getMovementsByMovementType(MovementType movementType)
    {
        List<Movement> movements = movementRepository.findByMovementType(movementType);

        String movementTypeString = MovementType.INGRESO.equals(movementType) ? "ingresos" : "salidas";

        MessageResponse response = MessageResponse.builder()
            .message("Lista de " + movementTypeString)
            .error(false)
            .data(movements)
            .build();
            
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> getMovement(Long id) 
    {
        Movement movement = movementRepository.findById(id).orElse(null);
        if(movement != null)
        {
            MessageResponse response = MessageResponse.builder()
                .message("Movimiento encontrado")
                .error(false)
                .data(movement)
                .build();
            return new ResponseEntity<>(response,HttpStatus.OK);
        }

        MessageResponse response = MessageResponse.builder()
           .message("Movimiento no encontrado")
           .error(true)
           .data(null)
           .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @Transactional
    public ResponseEntity<?> saveMovement(Movement movement)
    {
         if(!productRepository.existsById(movement.getProduct().getId()))
         {
            movementRepository.save(movement);
            MessageResponse response = MessageResponse.builder()
                .message("El producto no existe")
                .error(true)
                .data(movement)
                .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
         }

        try {
            Product product = productRepository.findById(movement.getProduct().getId()).orElse(null);

            if(product.getStock() < movement.getQuantity())
            {
                movementRepository.save(movement);
                MessageResponse response = MessageResponse.builder()
                    .message("No hay suficientes unidades en el producto")
                    .error(true)
                    .data(movement)
                    .build();
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            product.setStock(product.getStock() - movement.getQuantity());
            productRepository.save(product);

            movementRepository.save(movement);
            MessageResponse response = MessageResponse.builder()
                .message("Movimiento creado")
                .error(false)
                .data(movement)
                .build();
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            MessageResponse response = MessageResponse.builder()
               .message("No se pudo crear el movimiento")
               .error(true)
               .data(null)
               .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> updateMovement(Movement movement)
    {
        try {
            movementRepository.save(movement);
            MessageResponse response = MessageResponse.builder()
                .message("Movimiento actualizado")
                .error(false)
                .data(movement)
                .build();
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            MessageResponse response = MessageResponse.builder()
            .message("No se pudo crear el movimiento")
            .error(true)
            .data(null)
            .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> deleteMovement(Long id)
    {
        if (!movementRepository.existsById(id)) {
            MessageResponse response = MessageResponse.builder()
                .message("El movimiento no existe")
                .error(true)
                .data(null)
                .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            movementRepository.deleteById(id);
            MessageResponse response = MessageResponse.builder()
                .message("Movimiento eliminado")
                .error(false)
                .data(null)
                .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e) {
            MessageResponse response = MessageResponse.builder()
                .message("No se pudo eliminar")
                .error(true)
                .data(null)
                .build();
                
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
