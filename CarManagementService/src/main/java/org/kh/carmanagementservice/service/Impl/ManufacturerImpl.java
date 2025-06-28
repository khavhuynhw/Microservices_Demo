package org.kh.carmanagementservice.service.Impl;

import lombok.RequiredArgsConstructor;
import org.kh.carmanagementservice.dto.ManufacturerRequestDTO;
import org.kh.carmanagementservice.dto.ManufacturerResponseDTO;
import org.kh.carmanagementservice.exception.ResourceNotFoundException;
import org.kh.carmanagementservice.model.Manufacturer;
import org.kh.carmanagementservice.repo.ManufacturerRepository;
import org.kh.carmanagementservice.service.ManufacturerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManufacturerImpl implements ManufacturerService {
    private final ManufacturerRepository manufacturerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ManufacturerResponseDTO> getAllManufacturers() {
        return manufacturerRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public ManufacturerResponseDTO getManufacturerById(Integer manufacturerId) {
        return manufacturerRepository.findById(manufacturerId)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Manufacturer", "id", manufacturerId.toString()));
    }

    @Override
    @Transactional
    public ManufacturerResponseDTO createManufacturer(ManufacturerRequestDTO request) {
        if (manufacturerRepository.findByManufacturerName(request.getManufacturerName()).isPresent()) {
            throw new IllegalArgumentException("Manufacturer with name " + request.getManufacturerName() + " already exists.");
        }
        Manufacturer manufacturer = new Manufacturer(
                null, // ID will be generated
                request.getManufacturerName(),
                request.getDescription(),
                request.getManufacturerCountry()
        );
        Manufacturer savedManufacturer = manufacturerRepository.save(manufacturer);
        return convertToDto(savedManufacturer);
    }

    @Override
    @Transactional
    public ManufacturerResponseDTO updateManufacturer(Integer manufacturerId, ManufacturerRequestDTO request) {
        Manufacturer manufacturer = manufacturerRepository.findById(manufacturerId)
                .orElseThrow(() -> new ResourceNotFoundException("Manufacturer", "id", manufacturerId.toString()));

        if (!manufacturer.getManufacturerName().equals(request.getManufacturerName()) &&
                manufacturerRepository.findByManufacturerName(request.getManufacturerName()).isPresent()) {
            throw new IllegalArgumentException("Manufacturer with name " + request.getManufacturerName() + " already exists.");
        }

        manufacturer.setManufacturerName(request.getManufacturerName());
        manufacturer.setDescription(request.getDescription());
        manufacturer.setManufacturerCountry(request.getManufacturerCountry());
        Manufacturer updatedManufacturer = manufacturerRepository.save(manufacturer);
        return convertToDto(updatedManufacturer);
    }

    @Override
    @Transactional
    public void deleteManufacturer(Integer manufacturerId) {
        if (!manufacturerRepository.existsById(manufacturerId)) {
            throw new ResourceNotFoundException("Manufacturer", "id", manufacturerId.toString());
        }
        manufacturerRepository.deleteById(manufacturerId);
    }

    private ManufacturerResponseDTO convertToDto(Manufacturer manufacturer) {
        return new ManufacturerResponseDTO(
                manufacturer.getManufacturerId(),
                manufacturer.getManufacturerName(),
                manufacturer.getDescription(),
                manufacturer.getManufacturerCountry()
        );
    }
}
