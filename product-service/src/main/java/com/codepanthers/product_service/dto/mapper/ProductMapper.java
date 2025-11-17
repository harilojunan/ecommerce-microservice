package com.codepanthers.product_service.dto.mapper;

import com.codepanthers.product_service.dto.ProductRequest;
import com.codepanthers.product_service.dto.ProductResponse;
import com.codepanthers.product_service.entity.Product;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductRequest productRequest);
    ProductResponse toResponse(Product product);
    List<ProductResponse> toResponseList(List<Product> productList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Product product, ProductRequest request);
}
