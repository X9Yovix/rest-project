package com.rest.project.controllers;

import com.rest.project.entities.CategoryEntity;
import com.rest.project.entities.ProductEntity;
import com.rest.project.services.CategoryService;
import com.rest.project.services.ProductService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<ProductEntity>> getAllProducts() {
        List<ProductEntity> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductEntity> getProductById(@PathVariable int id) {
        Optional<ProductEntity> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductEntity> createProduct(@RequestBody ProductEntity product, @PathParam(value = "categoryId") int categoryId) {
        Optional<CategoryEntity> categoryEntityOptional = categoryService.getCategoryById(categoryId);

        if (categoryEntityOptional.isPresent()) {
            CategoryEntity categoryEntity = categoryEntityOptional.get();
            product.setCategory(categoryEntity);
            ProductEntity createdProduct = productService.createProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductEntity> updateProduct(@PathVariable int id, @RequestBody ProductEntity product, @RequestParam(value = "categoryId") int categoryId) {
        Optional<ProductEntity> existingProductOptional = productService.getProductById(id);
        if (existingProductOptional.isPresent()) {
            ProductEntity existingProduct = existingProductOptional.get();

            Optional<CategoryEntity> newCategoryOptional = categoryService.getCategoryById(categoryId);
            if (newCategoryOptional.isPresent()) {
                existingProduct.setCategory(newCategoryOptional.get());
            } else {
                return ResponseEntity.notFound().build();
            }

            existingProduct.setName(product.getName());
            existingProduct.setPrice(product.getPrice());

            ProductEntity updatedProduct = productService.updateProduct(existingProduct);
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
