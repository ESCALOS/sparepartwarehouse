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

import com.nanoka.warehouse.Model.Entity.Category;
import com.nanoka.warehouse.Service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/category")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200"})
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    
    @GetMapping
    public ResponseEntity<?> getCategories()
    {
        return categoryService.getCategories();
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<?> getCategory(@PathVariable Long id)
    {
        return categoryService.getCategory(id);
    }

    @PostMapping()
    public ResponseEntity<?> saveCategory(@RequestBody Category category)
    {
        return categoryService.saveCategory(category);
    }

    @PutMapping()
    public ResponseEntity<?> updateCategory(@RequestBody Category category)
    {
        return categoryService.updateCategory(category);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id)
    {
        return categoryService.deleteCategory(id);
    }
}
