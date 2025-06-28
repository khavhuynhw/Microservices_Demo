package org.kh.customermanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfileUpdateRequestDTO {
    private String customerName;
    private String telephone;
    private LocalDate customerBirthday;
}
