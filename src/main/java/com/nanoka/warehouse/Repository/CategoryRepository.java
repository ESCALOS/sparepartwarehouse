package com.nanoka.warehouse.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.nanoka.warehouse.Model.Entity.Category;

public interface CategoryRepository extends CrudRepository<Category,Long> {
    List<Category> findAll();
    
    Optional<Category> findById(Long id);

    boolean existsByName(String name);

    @Query("SELECT COUNT(*) FROM Supplier s WHERE s.name = :name AND s.id != :id")
    int countCategoriesWithSameNameExceptCurrentCategory(@Param(value = "name") String name, @Param(value = "id") Long id);
}
