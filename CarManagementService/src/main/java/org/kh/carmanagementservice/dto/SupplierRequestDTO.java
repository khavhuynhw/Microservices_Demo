package org.kh.carmanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Request DTO for creating/updating a supplier
@Data @NoArgsConstructor @AllArgsConstructor
public class SupplierRequestDTO {
    private String supplierName;
    private String supplierDescription;
    private String supplierAddress;
}
