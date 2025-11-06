package com.codepanthers.product_service.duplicate;

import com.codepanthers.product_service.dto.ProductRequest;
import com.codepanthers.product_service.dto.ProductResponse;
import com.codepanthers.product_service.entity.Product;

import java.util.List;

public interface DuplicateDetectionHandler {

    List<ProductResponse> findDuplicates(ProductRequest request, List<Product> existingProducts);
}
