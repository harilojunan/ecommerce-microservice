package com.codepanthers.product_service.duplicate;

import com.codepanthers.product_service.dto.ProductRequest;
import com.codepanthers.product_service.dto.ProductResponse;
import com.codepanthers.product_service.dto.mapper.ProductMapper;
import com.codepanthers.product_service.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ExactNameHandler implements DuplicateDetectionHandler {

    private final ProductMapper productMapper;

    @Override
    public List<ProductResponse> findDuplicates(ProductRequest request, List<Product> existingProducts) {
        return existingProducts.stream()
                .filter(p -> p.getName().equalsIgnoreCase(productMapper.toEntity(request).getName()))
                .map(productMapper :: toResponse)
                .collect(Collectors.toList());
    }
}
