package com.codepanthers.product_service.service.impl;

import com.codepanthers.product_service.dto.ProductRequest;
import com.codepanthers.product_service.dto.ProductResponse;
import com.codepanthers.product_service.dto.mapper.ProductMapper;
import com.codepanthers.product_service.duplicate.GenericDuplicateStrategy;
import com.codepanthers.product_service.entity.Product;
import com.codepanthers.product_service.exception.DuplicateProductException;
import com.codepanthers.product_service.exception.ResourceNotFoundException;
import com.codepanthers.product_service.repo.ProductRepo;
import com.codepanthers.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final GenericDuplicateStrategy duplicateStrategy;
    private final ProductMapper productMapper;
    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public ProductResponse addProduct(ProductRequest request) {

        logger.info("Logging Stated!!! Inside Product Service @Save Product Method Running");
        List<Product> existingProducts = productRepo.findAll();
        List<ProductResponse> similarProducts = duplicateStrategy.findDuplicates(request, existingProducts);

        if(!similarProducts.isEmpty()){
            throw new DuplicateProductException("Duplicate product with Name/Price. " + "Existing products: " + similarProducts);
        }

        Product product = productMapper.toEntity(request);
        product.setCreatedDate(LocalDateTime.now());
        Product savedProduct = productRepo.save(product);

        return productMapper.toResponse(savedProduct);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        logger.info("Logging Stated!!! Inside Product Service @Get Product By Id Method Running");
        if(Objects.nonNull(id) && productRepo.findById(id).isEmpty()){
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }

        Product product = productRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return productMapper.toResponse(product);
    }

    @Override
    public List<ProductResponse> getProducts() {
        logger.info("Logging Stated!!! Inside Product Service @Get All Product Method Running");
        return productRepo.findAll()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    public ProductResponse updateProduct(ProductRequest request) {
        logger.info("Logging Stated!!! Inside Product Service @Update Product Method Running");
        if(Objects.nonNull(productMapper.toEntity(request).getId()) && productRepo.findById(productMapper.toEntity(request).getId()).isEmpty()){
            throw new ResourceNotFoundException("Product not found with id: " + productMapper.toEntity(request).getId());
        }

        Product product = productRepo.findById(productMapper.toEntity(request).getId()).get();
        product.setId(productMapper.toEntity(request).getId());
        Product savedProduct = productRepo.save(product);

        return productMapper.toResponse(savedProduct);
    }

    @Override
    public String deleteProductById(Long id) {
        logger.info("Logging Stated!!! Inside Product Service @Delete Product Method Running");
        if(productRepo.findById(id).isEmpty()){
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepo.deleteById(id);
        return "Employee Deleted Successfully with id : "+id;
    }
}
