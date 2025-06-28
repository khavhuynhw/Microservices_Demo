package org.kh.rentingtransactionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentingTransactionResponseDTO {
    private Integer rentingTransactionId;
    private LocalDate rentingDate;
    private Double totalPrice;
    private Integer customerId;
    private String rentingStatus;
    private List<RentingDetailResponseDTO> rentingDetails;
}