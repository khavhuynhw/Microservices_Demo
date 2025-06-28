package org.kh.carmanagementservice.service;

import org.kh.carmanagementservice.dto.SupplierRequestDTO;
import org.kh.carmanagementservice.dto.SupplierResponseDTO;

import java.util.List;

public interface SupplierService {

    List<SupplierResponseDTO> getAllSuppliers();
    SupplierResponseDTO getSupplierById(Integer supplierId);
    SupplierResponseDTO createSupplier(SupplierRequestDTO request);
    SupplierResponseDTO updateSupplier(Integer supplierId, SupplierRequestDTO request);
    void deleteSupplier(Integer supplierId);
}
