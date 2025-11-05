package com.codepanthers.product_service.service;

import com.codepanthers.product_service.dto.ProductRequest;
import com.codepanthers.product_service.dto.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse addProduct(ProductRequest request);
    ProductResponse getProductById(Long id);
    List<ProductResponse> getProducts();
    ProductResponse updateProduct(ProductRequest request);
    void deleteProductById(Long id);

}
