package org.kh.rentingtransactionservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentingTransactionCreateRequestDTO {
    // customerId will be provided by the security context (e.g., from JWT in header)
    @NotEmpty(message = "Renting details cannot be empty")
    @Valid // Ensure validation applies to nested RentingDetailCreateDTOs
    private List<RentingDetailCreateDTO> rentingDetails;
}