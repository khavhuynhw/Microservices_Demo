package org.kh.rentingtransactionservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentingStatusUpdateRequestDTO {
    @NotBlank(message = "New status cannot be empty")
    private String newStatus; // e.g., "CONFIRMED", "COMPLETED", "CANCELLED", "REJECTED"
}
