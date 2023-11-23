package com.nanoka.warehouse.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nanoka.warehouse.Model.Entity.Category;
import com.nanoka.warehouse.Model.Payload.MessageResponse;
import com.nanoka.warehouse.Repository.CategoryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public ResponseEntity<?> getCategories()
    {
        List<Category> categories = categoryRepository.findAll();

        MessageResponse response = MessageResponse.builder()
            .message("Lista de categorías")
            .error(false)
            .data(categories)
            .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> getCategory(Long id) 
    {
        Category Category = categoryRepository.findById(id).orElse(null);
        if(Category != null)
        {
            MessageResponse response = MessageResponse.builder()
                .message("Categoría encontrada")
                .error(false)
                .data(Category)
                .build();
            return new ResponseEntity<>(response,HttpStatus.OK);
        }

        MessageResponse response = MessageResponse.builder()
           .message("Categoría no encontrada")
           .error(true)
           .data(null)
           .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @Transactional
    public ResponseEntity<?> saveCategory(Category Category)
    {
        if(categoryRepository.existsByName(Category.getName())) {
            MessageResponse response = MessageResponse.builder()
               .message("La categoría ya existe")
               .error(true)
               .data(null)
               .build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        
        try {
            categoryRepository.save(Category);
            
            MessageResponse response = MessageResponse.builder()
                .message("Categoría creada")
                .error(false)
                .data(Category)
                .build();
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            MessageResponse response = MessageResponse.builder()
               .message("No se pudo crear la categoría")
               .error(true)
               .data(null)
               .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> updateCategory(Category Category)
    {
        if(categoryRepository.countCategoriesWithSameNameExceptCurrentCategory(Category.getName(), Category.getId()) > 0) {
            MessageResponse response = MessageResponse.builder()
               .message("La categoría ya existe")
               .error(true)
               .data(null)
               .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        
        try {
            categoryRepository.save(Category);
            MessageResponse response = MessageResponse.builder()
                .message("Categoría actualizado")
                .error(false)
                .data(Category)
                .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            MessageResponse response = MessageResponse.builder()
                .message("No se pudo crear la categoría")
                .error(true)
                .data(null)
                .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> deleteCategory(Long id)
    {
        if (!categoryRepository.existsById(id)) {
            MessageResponse response = MessageResponse.builder()
                .message("La categoría no existe")
                .error(true)
                .data(null)
                .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            MessageResponse response = MessageResponse.builder()
                .message("No se pudo eliminar")
                .error(true)
                .data(null)
                .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            MessageResponse response = MessageResponse.builder()
                .message("Categoría eliminada")
                .error(false)
                .data(null)
                .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
