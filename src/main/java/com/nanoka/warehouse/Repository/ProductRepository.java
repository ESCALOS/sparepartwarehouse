package com.nanoka.warehouse.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.nanoka.warehouse.Model.Entity.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findAll();
    
    Optional<Product> findById(Long id);

    boolean existsByName(String name);

    @Query("SELECT COUNT(*) FROM Product p WHERE p.name = :name AND p.id != :id")
    int countProductsWithSameNameExceptCurrentProduct(@Param(value = "name") String name, @Param(value = "id") Long id);
}
