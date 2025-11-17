package com.codepanthers.product_service.service.impl;

import com.codepanthers.product_service.dto.ProductRequest;
import com.codepanthers.product_service.dto.ProductResponse;
import com.codepanthers.product_service.dto.mapper.ProductMapper;
import com.codepanthers.product_service.duplicate.GenericDuplicateStrategy;
import com.codepanthers.product_service.entity.Product;
import com.codepanthers.product_service.exception.DuplicateProductException;
import com.codepanthers.product_service.exception.InvalidArgumentException;
import com.codepanthers.product_service.exception.ResourceNotFoundException;
import com.codepanthers.product_service.repo.ProductRepo;
import com.codepanthers.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "id",
            "productCode",
            "name",
            "price",
            "createdDate"
    );
    private final ProductRepo productRepo;
    private final GenericDuplicateStrategy duplicateStrategy;
    private final ProductMapper productMapper;
    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public ProductResponse addProduct(ProductRequest request) {

        logger.debug("Start addProduct(): name={}, price={}", request.name(), request.price());

        if (duplicateStrategy.hasDuplicates(request)) {
            logger.warn("Duplicate product detected: name={}, code={}, price={}",
                    request.name(), request.productCode(), request.price());
            throw new DuplicateProductException("Duplicate product exists with same Name/Code/Price.");
        }

        Product product = productMapper.toEntity(request);
        product.setCreatedDate(LocalDateTime.now());

        Product savedProduct = productRepo.save(product);
        logger.info("Product created successfully with id={}", savedProduct.getId());

        return productMapper.toResponse(savedProduct);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        if (id == null) {
            throw new InvalidArgumentException("Product id cannot be null or empty");
        }

        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        logger.debug("Fetched product by ID:{}", id);
        return productMapper.toResponse(product);
    }

    @Override
    public ProductResponse getProductByCode(String productCode) {
        if (!StringUtils.hasText(productCode)) {
            throw new InvalidArgumentException("Product code cannot be null or empty");
        }

        Product product = productRepo.findByProductCode(productCode)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with code: " + productCode));

        logger.debug("Fetched product by code={} ", productCode);
        return productMapper.toResponse(product);
    }

    @Override
    public Page<ProductResponse> getProducts(int page, int size, String[] sort) {
        Pageable pageable = createPagination(page, size, sort);

        Page<Product> productPage = productRepo.findAll(pageable);

        logPageInfo(productPage, page, size);

        return productPage.map(productMapper::toResponse);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product existing = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        logger.debug("Updating product id={}", id);

        productMapper.updateEntity(existing, request);
        existing.setUpdatedDate(LocalDateTime.now());

        Product updatedProduct = productRepo.save(existing);
        return productMapper.toResponse(updatedProduct);
    }

    @Override
    public String deleteProductById(Long id) {
        if (!productRepo.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }

        logger.debug("Deleting product id={}", id);
        productRepo.deleteById(id);

        return "Employee Deleted Successfully with id : " + id;
    }

    @Override
    public long getProductsCount() {
        long count = productRepo.count();
        logger.debug("Number of total products: {}", count);
        return count;
    }

    @Override
    public long getCountsBetweenDates(LocalDateTime start, LocalDateTime end) {
        long count = productRepo.findByCountsBetweenDates(start, end);
        logger.debug("Number of products between dates {} and {}", start, end);
        return count;
    }

    @Override
    public long getCountsBetweenPrices(Double priceStart, Double priceEnd) {
        long count = productRepo.findByCountsBetweenPrices(priceStart, priceEnd);
        logger.debug("Number of products between prices {} and {}", priceStart, priceEnd);
        return count;
    }

    @Override
    public Page<ProductResponse> getProductsBetweenPrice(Double priceStart, Double priceEnd, int page, int size, String[] sort) {
        validatePriceRange(priceStart, priceEnd);
        Pageable pageable = createPagination(page, size, sort);
        logger.debug("Fetching products between prices {} - {}", priceStart, priceEnd);
        Page<Product> productPage = productRepo.findByProductsBetweenPrice(priceStart, priceEnd, pageable);

        if (productPage.isEmpty()) {
            throw new ResourceNotFoundException("No products found in price range: " + priceStart + " - " + priceEnd);
        }
        logPageInfo(productPage, page, size);

        return productPage.map(productMapper::toResponse);
    }

    @Override
    public Page<ProductResponse> getProductsBetweenDates(LocalDateTime startDate, LocalDateTime endDate, int page, int size, String[] sort) {
        validateDateRange(startDate, endDate);
        Pageable pageable = createPagination(page, size, sort);
        logger.debug("Fetching products between dates {} - {}", startDate, endDate);
        Page<Product> productPage = productRepo.findByProductsBetweenDates(startDate, endDate, pageable);

        if (productPage.isEmpty()) {
            throw new ResourceNotFoundException("No products found between dates: " + startDate + " - " + endDate);
        }
        logPageInfo(productPage, page, size);

        return productPage.map(productMapper::toResponse);
    }

    private Pageable createPagination(int page, int size, String[] sort) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Invalid pagination values: page=" + page + ", size=" + size);
        }

        String sortField = "id";
        String sortDirection = "asc";

        if (sort != null) {
            if (sort.length > 0 && ALLOWED_SORT_FIELDS.contains(sort[0])) {
                sortField = sort[0];
            }

            if (sort.length > 1) {
                sortDirection = sort[1];
            }
        }

        Sort.Direction direction =
                sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        return PageRequest.of(page, size, Sort.by(direction, sortField));
    }

    private void validatePriceRange(Double priceStart, Double priceEnd) {
        if (Double.isNaN(priceStart) || Double.isNaN(priceEnd)) {
            throw new IllegalArgumentException("Price range values cannot be null");
        }
        if (Double.isInfinite(priceStart) || Double.isInfinite(priceEnd)) {
            throw new IllegalArgumentException("Price values cannot be infinite");
        }
        if (priceStart < 0 || priceEnd < 0) {
            throw new IllegalArgumentException("Price values cannot be negative");
        }

        if (priceStart > priceEnd) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }
    }

    private void validateDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Date range values cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be greater than end date");
        }
    }

    private void logPageInfo(Page<?> pageData, int page, int size) {
        if (!logger.isDebugEnabled()) return;

        logger.debug("Page {} (size{}): totalElements={}, totalPages={}, sampleIds={}",
                page,
                size,
                pageData.getTotalElements(),
                pageData.getTotalPages(),
                pageData.getContent()
                        .stream()
                        .map(entity -> {
                            try {
                                return entity.getClass()
                                        .getMethod("getId")
                                        .invoke(entity);
                            } catch (Exception e) {
                                return "N/A";
                            }
                        })
                        .limit(5)
                        .toList()
        );
    }


}
