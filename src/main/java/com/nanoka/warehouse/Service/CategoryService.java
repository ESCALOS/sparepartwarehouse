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
        MessageResponse response;
        HttpStatus status;

        if(categoryRepository.existsByName(Category.getName())) {
            response = MessageResponse.builder()
               .message("La categoría ya existe")
               .error(true)
               .data(null)
               .build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Category categorySaved = categoryRepository.save(Category);

        if(categorySaved != null)
        {
            status = HttpStatus.CREATED;
            response = MessageResponse.builder()
                .message("Categoría creada")
                .error(false)
                .data(Category)
                .build();
                
        }else{
            status = HttpStatus.BAD_REQUEST;
            response = MessageResponse.builder()
               .message("No se pudo crear la categoría")
               .error(true)
               .data(null)
               .build();
        }

        return new ResponseEntity<>(response, status);
    }

    public ResponseEntity<?> updateCategory(Category Category)
    {
        MessageResponse response;
        HttpStatus status;

        if(categoryRepository.countCategoriesWithSameNameExceptCurrentCategory(Category.getName(), Category.getId()) > 0) {
            status = HttpStatus.BAD_REQUEST;
            response = MessageResponse.builder()
               .message("La categoría ya existe")
               .error(true)
               .data(null)
               .build();
        }else{
            
            Category categorySaved = categoryRepository.save(Category);

            if(categorySaved != null)
            {
                status = HttpStatus.CREATED;
                response = MessageResponse.builder()
                    .message("Categoría actualizado")
                    .error(false)
                    .data(Category)
                    .build();
                    
            }else{
                status = HttpStatus.BAD_REQUEST;
                response = MessageResponse.builder()
                .message("No se pudo crear la categoría")
                .error(true)
                .data(null)
                .build();
            }
        }
        return new ResponseEntity<>(response, status);
    }

    public ResponseEntity<?> deleteCategory(Long id)
    {
        MessageResponse response;
        HttpStatus status;

        if (!categoryRepository.existsById(id)) {
            response = MessageResponse.builder()
                .message("La categoría no existe")
                .error(true)
                .data(null)
                .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        categoryRepository.deleteById(id);

        if(categoryRepository.existsById(id))
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
                .message("Categoría eliminada")
                .error(false)
                .data(null)
                .build();
        }

        return new ResponseEntity<>(response, status);
    }
}
