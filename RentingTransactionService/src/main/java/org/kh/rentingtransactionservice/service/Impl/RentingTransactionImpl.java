package org.kh.rentingtransactionservice.service.Impl;

import lombok.RequiredArgsConstructor;
import org.kh.rentingtransactionservice.client.CarServiceFeignClient;
import org.kh.rentingtransactionservice.client.CustomerServiceFeignClient;
import org.kh.rentingtransactionservice.dto.*;
import org.kh.rentingtransactionservice.model.RentingDetail;
import org.kh.rentingtransactionservice.model.RentingTransaction;
import org.kh.rentingtransactionservice.repo.RentingDetailRepository;
import org.kh.rentingtransactionservice.repo.RentingTransactionRepository;
import org.kh.rentingtransactionservice.service.RentingTransactionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentingTransactionImpl  implements RentingTransactionService {
    private final RentingTransactionRepository rentingTransactionRepository;
    private final RentingDetailRepository rentingDetailRepository;
    private final CarServiceFeignClient carServiceFeignClient;
    private final CustomerServiceFeignClient customerServiceFeignClient;
    @Override
    public RentingTransactionResponseDTO createTransaction(RentingDetailCreateDTO request,String token) {
         var customerResponse = customerServiceFeignClient.getCustomerById(request.getCustomerId());
         if(customerResponse == null) {
             throw new RuntimeException("Customer not found");
         }
         double total = 0;
        List<RentingDetail> details = new ArrayList<>();
        var carResponse = carServiceFeignClient.getCarById(request.getCarId());
        if (carResponse.getCarStatus().compareToIgnoreCase("AVAILABLE") != 0) {
            throw new RuntimeException("Car is not available for renting");
        }
        if (carResponse == null) {
            throw new RuntimeException("Car not found with ID: " + request.getCarId());
        }
        double days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());
        double subtotal = days * carResponse.getCarRentingPricePerDay();
        total += subtotal;
        RentingDetail detail = new RentingDetail();
        detail.setCarId(carResponse.getCarId());
        detail.setStartDate(request.getStartDate());
        detail.setEndDate(request.getEndDate());
        detail.setPrice(total);
        details.add(detail);


        RentingTransaction transaction = new RentingTransaction();
        transaction.setCustomerId(request.getCustomerId());
        transaction.setRentingDate(LocalDate.now());
        transaction.setTotalPrice(total);
        transaction.setRentingStatus(RentingTransaction.RentingStatus.PENDING);
        transaction.setRentingDetails(details);

        details.forEach(d -> d.setRentingTransaction(transaction));
        RentingTransaction saved = rentingTransactionRepository.save(transaction);
        return new RentingTransactionResponseDTO(
                saved.getRentingTransactionId(),
                saved.getRentingDate(),
                saved.getTotalPrice(),
                saved.getCustomerId(),
                saved.getRentingStatus().name(),
                saved.getRentingDetails().stream().map(s -> new RentingDetailResponseDTO(
                        s.getRentingDetailId().toString(),
                        s.getCarId().toString(),
                        s.getStartDate(),
                        s.getEndDate(),
                        s.getPrice()
                )).toList()
        );
    }

    @Override
    public RentingTransactionResponseDTO getTransactionById(Integer id) {
        return rentingTransactionRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Transaction not found with ID: " + id));
    }

    @Override
    public List<RentingTransactionResponseDTO> findAllTransactions() {
        return rentingTransactionRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public RentingTransactionResponseDTO updateTransactionStatus(Integer transactionId, RentingStatusUpdateRequestDTO request,String token) {
        RentingTransaction transaction = rentingTransactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found with ID: " + transactionId));

        try {
            RentingTransaction.RentingStatus newStatus = RentingTransaction.RentingStatus.valueOf(request.getNewStatus().toUpperCase());
            if (request.getNewStatus().compareToIgnoreCase("APPROVED") == 0) {
                // Update car status to RENTED
                CarStatusUpdateFromRentingServiceDTO statusUpdate = new CarStatusUpdateFromRentingServiceDTO("RENTED");
                var carResponse = carServiceFeignClient.updateCarStatus(transaction.getRentingDetails().get(0).getCarId(), statusUpdate);
                if (carResponse == null) {
                    throw new RuntimeException("Failed to update car status to RENTED for car ID: " + transaction.getRentingDetails().get(0).getCarId());
                }
            } else if (request.getNewStatus().compareToIgnoreCase("COMPLETED") == 0) {
                // Update car status to AVAILABLE
                CarStatusUpdateFromRentingServiceDTO statusUpdate = new CarStatusUpdateFromRentingServiceDTO("AVAILABLE");
                var carResponse = carServiceFeignClient.updateCarStatus(transaction.getRentingDetails().get(0).getCarId(), statusUpdate);
                if (carResponse == null) {
                    throw new RuntimeException("Failed to update car status to AVAILABLE for car ID: " + transaction.getRentingDetails().get(0).getCarId());
                }
            }
            transaction.setRentingStatus(newStatus);
            RentingTransaction updated = rentingTransactionRepository.save(transaction);

            return toDto(updated);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + request.getNewStatus());
        }
    }

    @Override
    public RentingTransactionResponseDTO updateTransaction(Integer transactionId, RentingDetailCreateDTO request,String token) {
        RentingTransaction transaction = rentingTransactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found with ID: " + transactionId));

        // 2. Validate car exists
        var carResponse = carServiceFeignClient.getCarById(request.getCarId());
        if (carResponse == null) {
            throw new RuntimeException("Car not found with ID: " + request.getCarId());
        }

        // 3. Remove existing details
        transaction.getRentingDetails().clear();

        // 4. Recalculate price
        double days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());
        double subtotal = days * carResponse.getCarRentingPricePerDay();

        RentingDetail newDetail = new RentingDetail();
        newDetail.setCarId(request.getCarId());
        newDetail.setStartDate(request.getStartDate());
        newDetail.setEndDate(request.getEndDate());
        newDetail.setPrice(subtotal);
        newDetail.setRentingTransaction(transaction); // 🔁 set back-reference

        List<RentingDetail> newDetails = new ArrayList<>();
        newDetails.add(newDetail);

        transaction.setRentingDetails(newDetails);
        transaction.setTotalPrice(subtotal); // update total price
        transaction.setRentingStatus(RentingTransaction.RentingStatus.PENDING); // reset to PENDING if needed

        RentingTransaction updated = rentingTransactionRepository.save(transaction);

        return toDto(updated);
    }

    @Override
    public List<RentingTransactionResponseDTO> getRentingTransactionByPeriod(LocalDate startDate, LocalDate endDate) {
    if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
        throw new IllegalArgumentException("Invalid date range provided.");
    }
    return rentingTransactionRepository.findByRentingDateBetweenOrderByRentingDateDesc(startDate, endDate)
            .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<RentingTransactionResponseDTO> getTransactionByCustomerId(Integer authenticatedCustomerId) {
         return rentingTransactionRepository.findByCustomerId(authenticatedCustomerId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    public RentingTransactionResponseDTO toDto(RentingTransaction transaction) {
        return RentingTransactionResponseDTO.builder()
                .rentingTransactionId(transaction.getRentingTransactionId())
                .rentingDate(transaction.getRentingDate())
                .totalPrice(transaction.getTotalPrice())
                .customerId(transaction.getCustomerId())
                .rentingStatus(transaction.getRentingStatus().name())
                .rentingDetails(transaction.getRentingDetails().stream()
                        .map(detail -> RentingDetailResponseDTO.builder()
                                .rentingDetailId(detail.getRentingDetailId().toString())
                                .carId(detail.getCarId().toString())
                                .startDate(detail.getStartDate())
                                .endDate(detail.getEndDate())
                                .price(detail.getPrice())
                                .build())
                        .toList())
                .build();
    }
}
