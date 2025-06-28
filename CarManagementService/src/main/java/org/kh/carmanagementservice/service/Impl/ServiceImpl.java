package org.kh.carmanagementservice.service.Impl;

import lombok.RequiredArgsConstructor;
import org.kh.carmanagementservice.dto.SupplierRequestDTO;
import org.kh.carmanagementservice.dto.SupplierResponseDTO;
import org.kh.carmanagementservice.exception.ResourceNotFoundException;
import org.kh.carmanagementservice.model.Supplier;
import org.kh.carmanagementservice.repo.SupplierRepository;
import org.kh.carmanagementservice.service.SupplierService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponseDTO> getAllSuppliers() {
        return supplierRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponseDTO getSupplierById(Integer supplierId) {
        return supplierRepository.findById(supplierId)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", supplierId.toString()));
    }

    @Override
    @Transactional
    public SupplierResponseDTO createSupplier(SupplierRequestDTO request) {
        if (supplierRepository.findBySupplierName(request.getSupplierName()).isPresent()) {
            throw new IllegalArgumentException("Supplier with name " + request.getSupplierName() + " already exists.");
        }
        Supplier supplier = new Supplier(
                null, // ID will be generated
                request.getSupplierName(),
                request.getSupplierDescription(),
                request.getSupplierAddress()
        );
        Supplier savedSupplier = supplierRepository.save(supplier);
        return convertToDto(savedSupplier);
    }

    @Override
    @Transactional
    public SupplierResponseDTO updateSupplier(Integer supplierId, SupplierRequestDTO request) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", supplierId.toString()));

        if (!supplier.getSupplierName().equals(request.getSupplierName()) &&
                supplierRepository.findBySupplierName(request.getSupplierName()).isPresent()) {
            throw new IllegalArgumentException("Supplier with name " + request.getSupplierName() + " already exists.");
        }

        supplier.setSupplierName(request.getSupplierName());
        supplier.setSupplierDescription(request.getSupplierDescription());
        supplier.setSupplierAddress(request.getSupplierAddress());

        Supplier updatedSupplier = supplierRepository.save(supplier);
        return convertToDto(updatedSupplier);
    }

    @Override
    @Transactional
    public void deleteSupplier(Integer supplierId) {
        if (!supplierRepository.existsById(supplierId)) {
            throw new ResourceNotFoundException("Supplier", "id", supplierId.toString());
        }
        supplierRepository.deleteById(supplierId);
    }

    private SupplierResponseDTO convertToDto(Supplier supplier) {
        return new SupplierResponseDTO(
                supplier.getSupplierId(),
                supplier.getSupplierName(),
                supplier.getSupplierDescription(),
                supplier.getSupplierAddress()
        );
    }
}
