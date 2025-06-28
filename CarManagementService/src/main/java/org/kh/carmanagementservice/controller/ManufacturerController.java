package org.kh.carmanagementservice.controller;

import org.kh.carmanagementservice.dto.ManufacturerRequestDTO;
import org.kh.carmanagementservice.dto.ManufacturerResponseDTO;
import org.kh.carmanagementservice.service.ManufacturerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/manufacturers")
public class ManufacturerController {
    private final ManufacturerService manufacturerService;

    public ManufacturerController(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ManufacturerResponseDTO>> getAllManufacturers() {
        List<ManufacturerResponseDTO> manufacturers = manufacturerService.getAllManufacturers();
        return ResponseEntity.ok(manufacturers);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ManufacturerResponseDTO> getManufacturerById(@PathVariable Integer id) {
        ManufacturerResponseDTO manufacturer = manufacturerService.getManufacturerById(id);
        return ResponseEntity.ok(manufacturer);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ManufacturerResponseDTO> createManufacturer(@RequestBody ManufacturerRequestDTO request) {
        ManufacturerResponseDTO newManufacturer = manufacturerService.createManufacturer(request);
        return new ResponseEntity<>(newManufacturer, HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ManufacturerResponseDTO> updateManufacturer(@PathVariable Integer id, @RequestBody ManufacturerRequestDTO request) {
        ManufacturerResponseDTO updatedManufacturer = manufacturerService.updateManufacturer(id, request);
        return ResponseEntity.ok(updatedManufacturer);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManufacturer(@PathVariable Integer id) {
        manufacturerService.deleteManufacturer(id);
        return ResponseEntity.noContent().build();
    }
}
