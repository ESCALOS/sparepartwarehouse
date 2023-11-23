package com.nanoka.warehouse.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.nanoka.warehouse.Model.Entity.Movement;
import com.nanoka.warehouse.Model.Enum.MovementType;


public interface MovementRepository extends CrudRepository<Movement, Long> {
    List<Movement> findAll();

    List<Movement> findByMovementType(MovementType movementType);

    Optional<Movement> findById(Long id);

}
