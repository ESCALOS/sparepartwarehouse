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

import com.nanoka.warehouse.Model.Entity.Movement;
import com.nanoka.warehouse.Model.Enum.MovementType;
import com.nanoka.warehouse.Service.MovementService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/movement")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200"})
public class MovementController {
    @Autowired
    private MovementService movementService;
    
    @GetMapping
    public ResponseEntity<?> getMovements()
    {
        return movementService.getMovements();
    }

    @GetMapping(value = "input")
    public ResponseEntity<?> getInputs()
    {
        return movementService.getMovementsByMovementType(MovementType.INGRESO);
    }

    @GetMapping(value = "output")
    public ResponseEntity<?> getOutputs()
    {
        return movementService.getMovementsByMovementType(MovementType.SALIDA);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<?> getMovement(@PathVariable Long id)
    {
        return movementService.getMovement(id);
    }

    @PostMapping()
    public ResponseEntity<?> saveMovement(@RequestBody Movement movement)
    {
        return movementService.saveMovement(movement);
    }

    @PutMapping()
    public ResponseEntity<?> updateMovement(@RequestBody Movement movement)
    {
        return movementService.updateMovement(movement);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> deleteMovement(@PathVariable Long id)
    {
        return movementService.deleteMovement(id);
    }
}
