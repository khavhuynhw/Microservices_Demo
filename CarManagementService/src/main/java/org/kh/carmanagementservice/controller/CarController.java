package org.kh.carmanagementservice.controller;
import org.kh.carmanagementservice.dto.CarRequestDTO;
import org.kh.carmanagementservice.dto.CarResponseDTO;
import org.kh.carmanagementservice.dto.CarStatusUpdateRequestDTO;
import org.kh.carmanagementservice.service.CarInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cars")
public class CarController {
    private final CarInformationService carService;

    public CarController(CarInformationService carService) {
        this.carService = carService;
    }

    // --- Public/Customer Endpoints ---
    @GetMapping("/available")
    public ResponseEntity<List<CarResponseDTO>> getAvailableCars() {
        List<CarResponseDTO> cars = carService.findAllAvailableCars();
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponseDTO> getCarById(@PathVariable Integer id) {
        CarResponseDTO car = carService.getCarById(id);
        return ResponseEntity.ok(car);
    }

    // --- Admin Operations ---
    // @PreAuthorize("hasRole('ADMIN')") would be used here in a real app
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<CarResponseDTO>> getAllCars() {
        List<CarResponseDTO> cars = carService.getAllCars();
        return ResponseEntity.ok(cars);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CarResponseDTO> createCar(@RequestBody CarRequestDTO request) {
        CarResponseDTO newCar = carService.createCar(request);
        return new ResponseEntity<>(newCar, HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CarResponseDTO> updateCar(@PathVariable Integer id, @RequestBody CarRequestDTO request) {
        CarResponseDTO updatedCar = carService.updateCar(id, request);
        return ResponseEntity.ok(updatedCar);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Integer id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }

    // --- Internal/Inter-Service Endpoint (e.g., used by Renting Service) ---
    @PutMapping("/{id}/status")
    public ResponseEntity<CarResponseDTO> updateCarStatus(@PathVariable Integer id, @RequestBody CarStatusUpdateRequestDTO request) {
        CarResponseDTO updatedCar = carService.updateCarStatus(id, request);
        return ResponseEntity.ok(updatedCar);
    }
}
