package com.shipping.packagingapi.controller;

import com.shipping.packagingapi.dto.OrderRequest;
import com.shipping.packagingapi.dto.PackageResponse;
import com.shipping.packagingapi.model.Product;
import com.shipping.packagingapi.repository.ProductRepository;
import com.shipping.packagingapi.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final ProductRepository productRepository;
    private final OrderService orderService;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
        return ResponseEntity.ok(products);
    }

    @PostMapping("/orders/package")
    public ResponseEntity<List<PackageResponse>> placeOrder(@RequestBody OrderRequest orderRequest) {
        if (orderRequest == null || orderRequest.getProductIds() == null || orderRequest.getProductIds().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        List<PackageResponse> packages = orderService.processOrder(orderRequest.getProductIds());
        return ResponseEntity.ok(packages);
    }
}