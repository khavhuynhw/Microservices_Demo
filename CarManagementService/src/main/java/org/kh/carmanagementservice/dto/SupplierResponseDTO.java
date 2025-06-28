package org.kh.carmanagementservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierResponseDTO {
    private Integer supplierId;
    private String supplierName;
    private String supplierDescription;
    private String supplierAddress;
}
