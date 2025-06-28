package org.kh.customermanagementservice.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequestDTO {
    private Integer customerId;
    private  Integer userId;
    private String customerName;
    private String telephone;
    private String email;
    private LocalDate customerBirthday;
    private String customerStatus;
    private String role; // Assuming role is a String, adjust if it's an enum or another type
}
