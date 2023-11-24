package com.nanoka.warehouse.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nanoka.warehouse.Dto.MovementDto;
import com.nanoka.warehouse.Dto.UserDto;
import com.nanoka.warehouse.Model.Entity.Movement;
import com.nanoka.warehouse.Model.Entity.Product;
import com.nanoka.warehouse.Model.Entity.User;
import com.nanoka.warehouse.Model.Enum.MovementType;
import com.nanoka.warehouse.Model.Payload.MessageResponse;
import com.nanoka.warehouse.Repository.MovementRepository;
import com.nanoka.warehouse.Repository.ProductRepository;
import com.nanoka.warehouse.Repository.UserRepository;
import com.nanoka.warehouse.Service.Request.MovementRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovementService {
    @Autowired
    private MovementRepository movementRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

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
    public ResponseEntity<?> saveMovement(MovementRequest movementRequest, MovementType movementType, Principal principal)
    {
        try {
            Product product = productRepository.findById(movementRequest.getProductId()).orElse(null);
            User user = userRepository.findByUsername(principal.getName()).orElse(null);

            Movement movement = Movement.builder()
                    .product(product)
                    .quantity(movementRequest.getQuantity())
                    .price(movementRequest.getPrice())
                    .movementType(movementType)
                    .date(LocalDateTime.now())
                    .user(user)
                    .build();

            if(movementType.equals(MovementType.SALIDA))
            {
                if(product.getStock() < movementRequest.getQuantity())
                {
                    MessageResponse response = MessageResponse.builder()
                        .message("No hay suficientes unidades en el producto")
                        .error(true)
                        .data(movementRequest)
                        .build();
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }

                BigDecimal unitPrice = product.getPrice().divide(new BigDecimal(product.getStock()),2,RoundingMode.HALF_UP);
                BigDecimal totalPrice = unitPrice.multiply(new BigDecimal(movement.getQuantity()));

                movement.setPrice(totalPrice);

                product.setStock(product.getStock() - movement.getQuantity());
                product.setPrice(product.getPrice().subtract(totalPrice));
                
            }else{
                product.setStock(product.getStock() + movement.getQuantity());
                product.setPrice(product.getPrice().add(movement.getPrice()));
            }
            
            movementRepository.save(movement);
            productRepository.save(product);

            UserDto userDto = UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .role(user.getRole())
                .build();

            MovementDto movementDto = MovementDto.builder()
                .id(movement.getId())
                .product(product)
                .quantity(movement.getQuantity())
                .price(movement.getPrice())
                .type(movement.getMovementType())
                .date(movement.getDate())
                .user(userDto)
                .build();

            MessageResponse response = MessageResponse.builder()
                .message("Movimiento creado")
                .error(false)
                .data(movementDto)
                .build();
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            MessageResponse response = MessageResponse.builder()
               .message(e.getMessage())
               .error(true)
               .data(null)
               .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> updateMovement(MovementRequest movementRequest)
    {
        MessageResponse response = MessageResponse.builder()
        .message("Método el proceso")
        .error(true)
        .data(null)
        .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
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
            Movement movement = movementRepository.findById(id).orElse(null);
            Product product = productRepository.findById(movement.getProduct().getId()).orElse(null);

            //Regresar cantidades
            if(movement.getMovementType().equals(MovementType.SALIDA)){
                product.setStock(product.getStock() + movement.getQuantity());
                product.setPrice(product.getPrice().add(movement.getPrice()));
            }else{
                product.setStock(product.getStock() - movement.getQuantity());
                product.setPrice(product.getPrice().subtract(movement.getPrice()));
            }

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
