package com.rest.project.services;

import com.rest.project.entities.ProductEntity;
import com.rest.project.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<ProductEntity> getProductById(int id) {
        return productRepository.findById(id);
    }

    public ProductEntity createProduct(ProductEntity productEntity) {
        return productRepository.save(productEntity);
    }

    public ProductEntity updateProduct(ProductEntity productEntity) {
        return productRepository.save(productEntity);
    }

    public void deleteProduct(int id) {
        productRepository.deleteById(id);
    }
}
