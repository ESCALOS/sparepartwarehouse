package com.nanoka.warehouse.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.nanoka.warehouse.Model.Entity.Movement;

public interface MovementRepository extends CrudRepository<Movement, Long> {
    List<Movement> findAll();

    Optional<Movement> findById(Long id);

}
