package com.codepanthers.product_service.service;

import com.codepanthers.product_service.dto.ProductRequest;
import com.codepanthers.product_service.dto.ProductResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface ProductService {

    ProductResponse addProduct(ProductRequest request);

    ProductResponse getProductById(Long id);

    ProductResponse getProductByCode(String productCode);

    Page<ProductResponse> getProducts(int page, int size, String[] sort);

    ProductResponse updateProduct(Long id, ProductRequest request);

    String deleteProductById(Long id);

    long getProductsCount();

    long getCountsBetweenDates(LocalDateTime start, LocalDateTime end);

    long getCountsBetweenPrices(Double priceStart, Double priceEnd);

    Page<ProductResponse> getProductsBetweenPrice(Double priceStart, Double priceEnd, int page, int size, String[] sort);

    Page<ProductResponse> getProductsBetweenDates(LocalDateTime startDate, LocalDateTime endDate, int page, int size, String[] sort);


}
