package org.kh.rentingtransactionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentingDetailResponseDTO {
    private String rentingDetailId;
    private String carId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double price; // Calculated price for this detail
}