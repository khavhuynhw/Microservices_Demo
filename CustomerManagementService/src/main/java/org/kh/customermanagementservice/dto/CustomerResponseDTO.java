package org.kh.customermanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDTO {
    private Integer customerId;
    private String customerName;
    private String telephone;
    private String email;
    private LocalDate customerBirthday;
    private String customerStatus;
}
