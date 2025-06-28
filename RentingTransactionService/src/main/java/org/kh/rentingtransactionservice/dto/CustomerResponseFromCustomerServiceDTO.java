package org.kh.rentingtransactionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseFromCustomerServiceDTO {
    private String customerId;
    private String customerName;
    private String email;
}
