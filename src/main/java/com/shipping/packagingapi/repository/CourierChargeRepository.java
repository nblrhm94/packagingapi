package com.shipping.packagingapi.repository;

import com.shipping.packagingapi.model.CourierCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CourierChargeRepository extends JpaRepository<CourierCharge, Long> {

    @Query("SELECT c FROM CourierCharge c WHERE :weight >= c.weightFrom AND :weight <= c.weightTo")
    Optional<CourierCharge> findChargeForWeight(int weight);
}