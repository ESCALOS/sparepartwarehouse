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

import com.nanoka.warehouse.Service.ProductService;
import com.nanoka.warehouse.Service.Request.ProductRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/v1/product")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200"})
public class ProductController {
    @Autowired
    private ProductService productService;
    
    @GetMapping
    public ResponseEntity<?> getProducts()
    {
        return productService.getProducts();
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id)
    {
        return productService.getProduct(id);
    }

    @PostMapping()
    public ResponseEntity<?> saveProduct(@RequestBody ProductRequest productRequest)
    {
        return productService.saveProduct(productRequest);
    }

    @PutMapping()
    public ResponseEntity<?> updateProduct(@RequestBody ProductRequest productRequest)
    {
        return productService.updateProduct(productRequest);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id)
    {
        return productService.deleteProduct(id);
    }
}
