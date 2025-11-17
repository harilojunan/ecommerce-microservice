package com.codepanthers.product_service.repo;

import com.codepanthers.product_service.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    Optional<Product> findById(Long id);

    Optional<Product> findByProductCode(String productCode);

    List<Product> findByNameIgnoreCaseAndPrice(String name, Double price);

    boolean existsByCode(String code);

    Page<Product> findAll(Pageable pageable);

    long findByCountsBetweenDates(LocalDateTime start, LocalDateTime end);

    long findByCountsBetweenPrices(Double priceStart, Double priceEnd);

    Page<Product> findByProductsBetweenPrice(Double priceStart, Double priceEnd, Pageable pageable);

    Page<Product> findByProductsBetweenDates(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
