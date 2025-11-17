package com.codepanthers.product_service.controller;

import com.codepanthers.product_service.dto.ProductRequest;
import com.codepanthers.product_service.dto.ProductResponse;
import com.codepanthers.product_service.dto.mapper.ProductMapper;
import com.codepanthers.product_service.repo.ProductRepo;
import com.codepanthers.product_service.service.ProductService;
import com.codepanthers.product_service.util.AppConstance;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AppConstance.BASE_URL)
@RequiredArgsConstructor
public class ProductController {

    private final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductService service;
    private final ProductMapper productMapper;
    private final ProductService productService;

    @PostMapping(AppConstance.ADD_URL)
    public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody ProductRequest request) {
        logger.debug("Adding product with the product code {}", productMapper.toEntity(request).getProductCode());
        ProductResponse response = productService.addProduct(request);

        logger.info("Product added successfully!");
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(AppConstance.ALL_URL)
    public ResponseEntity<Page<ProductResponse>> allProduct(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "productCode,asc") String[] sort) {

        Page<ProductResponse> response = service.getProducts(page, size, sort);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(AppConstance.SEARCH_URL)
    public ResponseEntity<ProductResponse> searchProduct(@RequestParam String productCode) {
        logger.debug("Searching product with the product code {}", productCode);
//        ProductResponse response = service.getProductByCode(productCode);
        return null;
    }
}
