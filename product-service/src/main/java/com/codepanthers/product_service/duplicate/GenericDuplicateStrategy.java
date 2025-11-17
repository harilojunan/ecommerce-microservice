package com.codepanthers.product_service.duplicate;

import com.codepanthers.product_service.dto.ProductRequest;
import com.codepanthers.product_service.repo.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GenericDuplicateStrategy implements DuplicateDetectionHandler {

    private final ProductRepo productRepo;

    @Override
    public boolean hasDuplicates(ProductRequest request) {
        if (productRepo.existsByCode(request.productCode())) {
            return true;
        }
        return !productRepo.findByNameIgnoreCaseAndPrice(request.name(), request.price()).isEmpty();
    }
}
