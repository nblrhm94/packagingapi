package com.shipping.packagingapi.service;

import com.shipping.packagingapi.dto.PackageResponse;
import com.shipping.packagingapi.model.CourierCharge;
import com.shipping.packagingapi.model.Product;
import com.shipping.packagingapi.repository.CourierChargeRepository;
import com.shipping.packagingapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final CourierChargeRepository courierChargeRepository;
    private static final double MAX_PACKAGE_PRICE = 250.0;
    private static final double ORDER_SPLIT_THRESHOLD = 250.0;

    /**
     * Main entry point for processing an order and creating packages.
     * @param productIds A list of IDs for the ordered products.
     * @return A list of packaged responses, formatted for the client.
     */
    public List<PackageResponse> processOrder(List<Long> productIds) {
        List<Product> selectedProducts = productRepository.findAllById(productIds);
        if (selectedProducts.isEmpty()) {
            return Collections.emptyList();
        }

        double totalOrderPrice = selectedProducts.stream().mapToDouble(Product::getPrice).sum();

        if (totalOrderPrice <= ORDER_SPLIT_THRESHOLD) {
            return List.of(createPackageFromProducts(selectedProducts));
        } else {
            return createMultiplePackages(selectedProducts);
        }
    }

    /**
     * Implements the logic for splitting an order into multiple packages.
     * It prioritizes the price constraint by packing more expensive items first.
     * @param products The list of products to be packaged.
     * @return A list of packaged responses.
     */
    private List<PackageResponse> createMultiplePackages(List<Product> products) {
        products.sort(Comparator.comparingDouble(Product::getPrice).reversed());

        List<List<Product>> packages = new ArrayList<>();
        packages.add(new ArrayList<>());

        for (Product product : products) {
            boolean isPlaced = false;

            for (List<Product> pkg : packages) {
                double currentPackagePrice = pkg.stream().mapToDouble(Product::getPrice).sum();
                if (currentPackagePrice + product.getPrice() < MAX_PACKAGE_PRICE) {
                    pkg.add(product);
                    isPlaced = true;
                    break;
                }
            }

            if (!isPlaced) {
                List<Product> newPackage = new ArrayList<>();
                newPackage.add(product);
                packages.add(newPackage);
            }
        }

        return packages.stream()
                .filter(pkg -> !pkg.isEmpty())
                .map(this::createPackageFromProducts)
                .collect(Collectors.toList());
    }

    /**
     * A helper method to transform a list of products into a single, calculated PackageResponse.
     * @param products The products that form a single package.
     * @return A PackageResponse DTO with calculated totals.
     */
    private PackageResponse createPackageFromProducts(List<Product> products) {
        int totalWeight = products.stream().mapToInt(Product::getWeight).sum();
        double totalPrice = products.stream().mapToDouble(Product::getPrice).sum();
        double courierPrice = getCourierPrice(totalWeight);
        List<String> itemNames = products.stream().map(Product::getName).collect(Collectors.toList());

        return new PackageResponse(itemNames, totalWeight, totalPrice, courierPrice);
    }

    /**
     * Looks up the courier shipping price from the database based on weight.
     * @param weight The total weight of a package in grams.
     * @return The corresponding courier price, or 0.0 if no rate is found.
     */
    private double getCourierPrice(int weight) {
        return courierChargeRepository.findChargeForWeight(weight)
                .map(CourierCharge::getPrice)
                .orElse(0.0);
    }
}