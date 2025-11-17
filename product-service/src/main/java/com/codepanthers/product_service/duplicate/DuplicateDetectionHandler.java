package com.codepanthers.product_service.duplicate;

import com.codepanthers.product_service.dto.ProductRequest;

public interface DuplicateDetectionHandler {

    boolean hasDuplicates(ProductRequest request);
}
