package com.codepanthers.product_service.controller;

import com.codepanthers.product_service.dto.mapper.ProductMapper;
import com.codepanthers.product_service.service.ProductService;
import com.codepanthers.product_service.util.AppConstance;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppConstance.BASE_URL)
@RequiredArgsConstructor
public class ProductController {

    private final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductService service;
    private final ProductMapper productMapper;
}
