package org.kh.rentingtransactionservice.client;

import org.kh.rentingtransactionservice.dto.CarResponseFromCarServiceDTO;
import org.kh.rentingtransactionservice.dto.CarStatusUpdateFromRentingServiceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "car-management-service", path = "/api/v1/cars")
public interface CarServiceFeignClient {

    @GetMapping("/{id}")
    CarResponseFromCarServiceDTO getCarById(@PathVariable("id") Integer carId);

    @PutMapping("/{id}/status")
    CarResponseFromCarServiceDTO updateCarStatus(@PathVariable("id") Integer carId,
                                               @RequestBody CarStatusUpdateFromRentingServiceDTO carStatusUpdate);
} 