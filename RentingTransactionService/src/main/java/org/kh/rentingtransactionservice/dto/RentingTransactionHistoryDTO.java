package org.kh.rentingtransactionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentingTransactionHistoryDTO {
    private String rentingTransactionId;
    private LocalDate rentingDate;
    private Double totalPrice;
    private String rentingStatus;
    private int numberOfCars;
    private List<String> rentedCarNames; // A list of names for summary
}