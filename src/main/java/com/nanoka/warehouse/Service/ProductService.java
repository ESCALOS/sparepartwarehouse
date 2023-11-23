package com.nanoka.warehouse.Service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nanoka.warehouse.Model.Entity.Category;
import com.nanoka.warehouse.Model.Entity.Product;
import com.nanoka.warehouse.Model.Entity.Supplier;
import com.nanoka.warehouse.Model.Payload.MessageResponse;
import com.nanoka.warehouse.Repository.CategoryRepository;
import com.nanoka.warehouse.Repository.ProductRepository;
import com.nanoka.warehouse.Repository.SupplierRepository;
import com.nanoka.warehouse.Service.Request.ProductRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired 
    private CategoryRepository categoryRepository;

    @Autowired 
    private SupplierRepository supplierRepository;

    public ResponseEntity<?> getProducts()
    {
        List<Product> products = productRepository.findAll();

        MessageResponse response = MessageResponse.builder()
            .message("Lista de productos")
            .error(false)
            .data(products)
            .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> getProduct(Long id) 
    {
        Product product = productRepository.findById(id).orElse(null);
        if(product != null)
        {
            MessageResponse response = MessageResponse.builder()
                .message("Producto encontrado")
                .error(false)
                .data(product)
                .build();
            return new ResponseEntity<>(response,HttpStatus.OK);
        }

        MessageResponse response = MessageResponse.builder()
           .message("Producto no encontrado")
           .error(true)
           .data(null)
           .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @Transactional
    public ResponseEntity<?> saveProduct(ProductRequest productRequest)
    {
        if(productRepository.existsByName(productRequest.getName())) {
            MessageResponse response = MessageResponse.builder()
               .message("El producto ya existe")
               .error(true)
               .data(null)
               .build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if(!categoryRepository.existsById(productRequest.getCategoryId()))
        {
            MessageResponse response = MessageResponse.builder()
                .message("La categoría no existe")
                .error(true)
                .data(null)
                .build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if(!supplierRepository.existsById(productRequest.getSupplierId()))
        {
            MessageResponse response = MessageResponse.builder()
                .message("El proveedor no existe")
                .error(true)
                .data(null)
                .build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            Category category = categoryRepository.findById(productRequest.getCategoryId()).orElse(null);
            Supplier supplier = supplierRepository.findById(productRequest.getSupplierId()).orElse(null);

            Product product = Product.builder()
                .name(productRequest.getName())
                .measurementUnit(productRequest.getMeasurementUnit())
                .category(category)
                .supplier(supplier)
                .stock(0)
                .stockMin(productRequest.getStockMin())
                .price(BigDecimal.ZERO)
                .build();

            productRepository.save(product);

            MessageResponse response = MessageResponse.builder()
                .message("Producto creado")
                .error(false)
                .data(product)
                .build();
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            MessageResponse response = MessageResponse.builder()
               .message("No se pudo crear el producto")
               .error(true)
               .data(null)
               .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> updateProduct(ProductRequest productRequest)
    {
        if(productRepository.countProductsWithSameNameExceptCurrentProduct(productRequest.getName(), productRequest.getId()) > 0) {
            MessageResponse response = MessageResponse.builder()
               .message("El nombre del producto ya existe")
               .error(true)
               .data(null)
               .build();
               return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if(!categoryRepository.existsById(productRequest.getCategoryId())){
            MessageResponse response = MessageResponse.builder()
                .message("La categoría no existe")
                .error(true)
                .data(null)
                .build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if(!supplierRepository.existsById(productRequest.getSupplierId())){
            MessageResponse response = MessageResponse.builder()
                .message("El proveedor no existe")
                .error(true)
                .data(null)
                .build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            Category category = categoryRepository.findById(productRequest.getCategoryId()).orElse(null);
            Supplier supplier = supplierRepository.findById(productRequest.getSupplierId()).orElse(null);

            Product product = productRepository.findById(productRequest.getId()).orElse(null);

            product.setName(productRequest.getName());
            product.setMeasurementUnit(productRequest.getMeasurementUnit());
            product.setCategory(category);
            product.setSupplier(supplier);
            product.setStockMin(productRequest.getStockMin());

            productRepository.save(product);

            MessageResponse response = MessageResponse.builder()
                .message("Producto actualizado")
                .error(false)
                .data(product)
                .build();
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
                MessageResponse response = MessageResponse.builder()
                .message("No se pudo crear el producto")
                .error(true)
                .data(null)
                .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> deleteProduct(Long id)
    {
        if (!productRepository.existsById(id)) {
            MessageResponse response = MessageResponse.builder()
                .message("El producto no existe")
                .error(true)
                .data(null)
                .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            productRepository.deleteById(id);
            MessageResponse response = MessageResponse.builder()
                .message("Producto eliminado")
                .error(false)
                .data(null)
                .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            MessageResponse response = MessageResponse.builder()
                .message("No se pudo eliminar")
                .error(true)
                .data(null)
                .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
