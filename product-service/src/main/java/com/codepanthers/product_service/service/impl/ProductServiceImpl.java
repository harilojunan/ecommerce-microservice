package com.codepanthers.product_service.service.impl;

import com.codepanthers.product_service.dto.ProductRequest;
import com.codepanthers.product_service.dto.ProductResponse;
import com.codepanthers.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductService productService;

    @Override
    public ProductResponse addProduct(ProductRequest request) {
        return null;
    }

    @Override
    public ProductResponse getProductById(Long id) {
        return null;
    }

    @Override
    public List<ProductResponse> getProducts() {
        return List.of();
    }

    @Override
    public ProductResponse updateProduct(ProductRequest request) {
        return null;
    }

    @Override
    public void deleteProductById(Long id) {

    }
}
