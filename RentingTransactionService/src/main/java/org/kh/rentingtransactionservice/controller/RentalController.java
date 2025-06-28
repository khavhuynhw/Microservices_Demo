package org.kh.rentingtransactionservice.controller;

import org.apache.coyote.Response;
import org.kh.rentingtransactionservice.dto.*;
import org.kh.rentingtransactionservice.service.RentingTransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rentals")
public class RentalController {
    private final RentingTransactionService rentalService;

    public RentalController(RentingTransactionService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping("transaction")
    public ResponseEntity<RentingTransactionResponseDTO> createRentalTransaction(
            @RequestBody RentingDetailCreateDTO request,
            @RequestHeader("Authorization") String authHeader
            ) {
        String token = authHeader.replace("Bearer ", "");
        RentingTransactionResponseDTO response = rentalService.createTransaction(request,token);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<RentingTransactionResponseDTO>> getAllRentalTransactions() {
        List<RentingTransactionResponseDTO> response = rentalService.findAllTransactions();
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/period")
    public ResponseEntity<List<RentingTransactionResponseDTO>> getRentingTransactionByPeriod(
            @RequestBody PeriodSearchDTO request) {
        List<RentingTransactionResponseDTO> response = rentalService.getRentingTransactionByPeriod(request.getStartDate(), request.getEndDate());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<RentingTransactionResponseDTO> getRentalTransactionById(
            @PathVariable Integer transactionId) {
        RentingTransactionResponseDTO response = rentalService.getTransactionById(transactionId);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<RentingTransactionResponseDTO>> getRentalTransactionByCustomerId(
            @PathVariable Integer customerId) {
        List<RentingTransactionResponseDTO> response = rentalService.getTransactionByCustomerId(customerId);
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{transactionId}/status")
    public ResponseEntity<RentingTransactionResponseDTO> updateRentalTransactionStatus(
            @PathVariable Integer transactionId,
            @RequestHeader("Authorization") String authHeader,
            @RequestBody RentingStatusUpdateRequestDTO request) {
        String token = authHeader.replace("Bearer ", "");
        RentingTransactionResponseDTO response = rentalService.updateTransactionStatus(transactionId, request,token);
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{transactionId}/update")
    public ResponseEntity<RentingTransactionResponseDTO> updateRentalTransaction(
            @PathVariable Integer transactionId,
            @RequestHeader("Authorization") String authHeader,
            @RequestBody RentingDetailCreateDTO request) {
        String token = authHeader.replace("Bearer ", "");
        RentingTransactionResponseDTO response = rentalService.updateTransaction(transactionId, request,token);
        return ResponseEntity.ok(response);
    }
}
