package com.shipping.packagingapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageResponse {
    private List<String> items;
    private int totalWeight;
    private double totalPrice;
    private double courierPrice;
}