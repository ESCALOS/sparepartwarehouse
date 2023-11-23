package com.nanoka.warehouse.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nanoka.warehouse.Model.Entity.Movement;
import com.nanoka.warehouse.Model.Payload.MessageResponse;
import com.nanoka.warehouse.Repository.MovementRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovementService {
    @Autowired
    private MovementRepository movementRepository;

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
        MessageResponse response;
        HttpStatus status;

        Movement movementSaved = movementRepository.save(movement);

        if(movementSaved != null)
        {
            status = HttpStatus.CREATED;
            response = MessageResponse.builder()
                .message("Movimiento creado")
                .error(false)
                .data(movement)
                .build();
                
        }else{
            status = HttpStatus.BAD_REQUEST;
            response = MessageResponse.builder()
               .message("No se pudo crear el movimiento")
               .error(true)
               .data(null)
               .build();
        }

        return new ResponseEntity<>(response, status);
    }

    public ResponseEntity<?> updateMovement(Movement movement)
    {
        MessageResponse response;
        HttpStatus status;

        Movement movementSaved = movementRepository.save(movement);

        if(movementSaved != null)
        {
            status = HttpStatus.CREATED;
            response = MessageResponse.builder()
                .message("Movimiento actualizado")
                .error(false)
                .data(movement)
                .build();
                
        }else{
            status = HttpStatus.BAD_REQUEST;
            response = MessageResponse.builder()
            .message("No se pudo crear el movimiento")
            .error(true)
            .data(null)
            .build();
        }
        return new ResponseEntity<>(response, status);
    }

    public ResponseEntity<?> deleteMovement(Long id)
    {
        MessageResponse response;
        HttpStatus status;

        if (!movementRepository.existsById(id)) {
            response = MessageResponse.builder()
                .message("El movimiento no existe")
                .error(true)
                .data(null)
                .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        movementRepository.deleteById(id);

        if(movementRepository.existsById(id))
        {
            status = HttpStatus.BAD_REQUEST;
            response = MessageResponse.builder()
                .message("No se pudo eliminar")
                .error(true)
                .data(null)
                .build();
        }else{
            status = HttpStatus.OK;
            response = MessageResponse.builder()
                .message("Movimiento eliminado")
                .error(false)
                .data(null)
                .build();
        }

        return new ResponseEntity<>(response, status);
    }
}
