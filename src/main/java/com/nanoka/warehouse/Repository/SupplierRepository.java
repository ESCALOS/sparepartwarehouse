package com.nanoka.warehouse.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.nanoka.warehouse.Model.Entity.Supplier;

public interface SupplierRepository extends CrudRepository<Supplier,Long>{

    List<Supplier> findAll();

    boolean existsByName(String name);
    
    @Query("SELECT COUNT(*) FROM Supplier s WHERE s.name = :name AND s.id != :id")
    int countSuppliersWithSameNameExceptCurrentUser(@Param(value = "name") String name, @Param(value = "id") Long id);
}
