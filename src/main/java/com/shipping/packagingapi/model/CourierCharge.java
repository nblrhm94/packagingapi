package com.shipping.packagingapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "courier_charges")
@Data
@NoArgsConstructor
public class CourierCharge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int weightFrom;
    private int weightTo;
    private double price;
}