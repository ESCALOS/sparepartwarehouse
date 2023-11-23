package com.nanoka.warehouse.Service;

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
        MessageResponse response;
        HttpStatus status;

        if(productRepository.existsByName(productRequest.getName())) {
            response = MessageResponse.builder()
               .message("El producto ya existe")
               .error(true)
               .data(null)
               .build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if(!categoryRepository.existsById(productRequest.getCategoryId()))
        {
            response = MessageResponse.builder()
                .message("La categoría no existe")
                .error(true)
                .data(null)
                .build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if(!supplierRepository.existsById(productRequest.getSupplierId()))
        {
            response = MessageResponse.builder()
                .message("El proveedor no existe")
                .error(true)
                .data(null)
                .build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Category category = categoryRepository.findById(productRequest.getCategoryId()).orElse(null);
        Supplier supplier = supplierRepository.findById(productRequest.getSupplierId()).orElse(null);

        Product product = Product.builder()
            .name(productRequest.getName())
            .measurementUnit(productRequest.getMeasurementUnit())
            .category(category)
            .supplier(supplier)
            .stock(productRequest.getStock())
            .stockMin(productRequest.getStockMin())
            .price(productRequest.getPrice())
            .build();


        Product productSaved = productRepository.save(product);

        if(productSaved != null)
        {
            status = HttpStatus.CREATED;
            response = MessageResponse.builder()
                .message("Producto creado")
                .error(false)
                .data(productSaved)
                .build();
                
        }else{
            status = HttpStatus.BAD_REQUEST;
            response = MessageResponse.builder()
               .message("No se pudo crear el producto")
               .error(true)
               .data(null)
               .build();
        }

        return new ResponseEntity<>(response, status);
    }

    public ResponseEntity<?> updateProduct(ProductRequest productRequest)
    {
        MessageResponse response;
        HttpStatus status;

        if(productRepository.countProductsWithSameNameExceptCurrentProduct(productRequest.getName(), productRequest.getId()) > 0) {
            status = HttpStatus.BAD_REQUEST;
            response = MessageResponse.builder()
               .message("El producto ya existe")
               .error(true)
               .data(null)
               .build();
        }else{
            if(productRepository.existsByName(productRequest.getName())) {
                response = MessageResponse.builder()
                .message("El producto ya existe")
                .error(true)
                .data(null)
                .build();

                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            if(!categoryRepository.existsById(productRequest.getCategoryId())){
                response = MessageResponse.builder()
                    .message("La categoría no existe")
                    .error(true)
                    .data(null)
                    .build();

                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            Category category = categoryRepository.findById(productRequest.getCategoryId()).orElse(null);
            Supplier supplier = supplierRepository.findById(productRequest.getSupplierId()).orElse(null);

            Product product = Product.builder()
                .name(productRequest.getName())
                .measurementUnit(productRequest.getMeasurementUnit())
                .category(category)
                .supplier(supplier)
                .stock(productRequest.getStock())
                .stockMin(productRequest.getStockMin())
                .price(productRequest.getPrice())
                .build();

            Product productUpdated = productRepository.save(product);

            if(productUpdated != null)
            {
                status = HttpStatus.CREATED;
                response = MessageResponse.builder()
                    .message("Producto actualizado")
                    .error(false)
                    .data(productUpdated)
                    .build();
                    
            }else{
                status = HttpStatus.BAD_REQUEST;
                response = MessageResponse.builder()
                .message("No se pudo crear el producto")
                .error(true)
                .data(null)
                .build();
            }
        }
        return new ResponseEntity<>(response, status);
    }

    public ResponseEntity<?> deleteProduct(Long id)
    {
        MessageResponse response;
        HttpStatus status;

        if (!productRepository.existsById(id)) {
            response = MessageResponse.builder()
                .message("El producto no existe")
                .error(true)
                .data(null)
                .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        productRepository.deleteById(id);

        if(productRepository.existsById(id))
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
                .message("Producto eliminado")
                .error(false)
                .data(null)
                .build();
        }

        return new ResponseEntity<>(response, status);
    }
}
