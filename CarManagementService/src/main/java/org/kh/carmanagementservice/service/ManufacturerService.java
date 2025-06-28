package org.kh.carmanagementservice.service;

import org.kh.carmanagementservice.dto.ManufacturerRequestDTO;
import org.kh.carmanagementservice.dto.ManufacturerResponseDTO;

import java.util.List;

public interface ManufacturerService {
    List<ManufacturerResponseDTO> getAllManufacturers();
    ManufacturerResponseDTO getManufacturerById(Integer manufacturerId);
    ManufacturerResponseDTO createManufacturer(ManufacturerRequestDTO request);
    ManufacturerResponseDTO updateManufacturer(Integer manufacturerId, ManufacturerRequestDTO request);
    void deleteManufacturer(Integer manufacturerId);
}
