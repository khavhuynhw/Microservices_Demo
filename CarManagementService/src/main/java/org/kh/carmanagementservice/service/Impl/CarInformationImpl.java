package org.kh.carmanagementservice.service.Impl;

import lombok.RequiredArgsConstructor;
import org.kh.carmanagementservice.dto.CarRequestDTO;
import org.kh.carmanagementservice.dto.CarResponseDTO;
import org.kh.carmanagementservice.dto.CarStatusUpdateRequestDTO;
import org.kh.carmanagementservice.exception.ResourceNotFoundException;
import org.kh.carmanagementservice.model.CarInformation;
import org.kh.carmanagementservice.model.Manufacturer;
import org.kh.carmanagementservice.model.Supplier;
import org.kh.carmanagementservice.repo.CarInformationRepository;
import org.kh.carmanagementservice.repo.ManufacturerRepository;
import org.kh.carmanagementservice.repo.SupplierRepository;
import org.kh.carmanagementservice.service.CarInformationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarInformationImpl implements CarInformationService {
    private final CarInformationRepository carInformationRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final SupplierRepository supplierRepository;

    // --- Public/Customer Operations ---

    @Override
    @Transactional(readOnly = true)
    public List<CarResponseDTO> findAllAvailableCars() {
        return carInformationRepository.findByCarStatus("AVAILABLE")
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CarResponseDTO getCarById(Integer carId) {
        return carInformationRepository.findById(carId)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Car", "id", carId.toString()));
    }

    // --- Admin Operations ---

    @Override
    @Transactional(readOnly = true)
    public List<CarResponseDTO> getAllCars() {
        return carInformationRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    @Transactional
    public CarResponseDTO createCar(CarRequestDTO request) {
        Manufacturer manufacturer = manufacturerRepository.findById(request.getManufacturerId())
                .orElseThrow(() -> new ResourceNotFoundException("Manufacturer", "id", request.getManufacturerId().toString()));
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", request.getSupplierId().toString()));

        CarInformation car = new CarInformation(
                null, // ID will be generated
                request.getCarName(),
                request.getCarDescription(),
                request.getNumberOfDoors(),
                request.getSeatingCapacity(),
                CarInformation.FuelType.valueOf(request.getFuelType().toUpperCase()),
                request.getYear(),
                manufacturer,
                supplier,
                CarInformation.CarStatus.valueOf(request.getCarStatus().toUpperCase()),
                request.getCarRentingPricePerDay()
        );
        CarInformation savedCar = carInformationRepository.save(car);
        return convertToDto(savedCar);
    }

    @Override
    @Transactional
    public CarResponseDTO updateCar(Integer carId, CarRequestDTO request) {
        CarInformation car = carInformationRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car", "id", carId.toString()));

        Manufacturer manufacturer = manufacturerRepository.findById(request.getManufacturerId())
                .orElseThrow(() -> new ResourceNotFoundException("Manufacturer", "id", request.getManufacturerId().toString()));
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", "id", request.getSupplierId().toString()));

        car.setCarName(request.getCarName());
        car.setCarDescription(request.getCarDescription());
        car.setNumberOfDoors(request.getNumberOfDoors());
        car.setSeatingCapacity(request.getSeatingCapacity());
        car.setFuelType(CarInformation.FuelType.valueOf(request.getFuelType()));
        car.setYear(request.getYear());
        car.setManufacturer(manufacturer);
        car.setSupplier(supplier);
        car.setCarStatus(CarInformation.CarStatus.valueOf(request.getCarStatus()));
        car.setCarRentingPricePerDay(request.getCarRentingPricePerDay());

        CarInformation updatedCar = carInformationRepository.save(car);
        return convertToDto(updatedCar);
    }

    @Override
    @Transactional
    public void deleteCar(Integer carId) {
        CarInformation car = carInformationRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car", "id", carId.toString()));
        car.setCarStatus(CarInformation.CarStatus.SUSPENDED);
        carInformationRepository.save(car);
    }

    // --- Internal/Inter-Service Operations (e.g., called by Renting Service) ---

    @Override
    @Transactional
    public CarResponseDTO updateCarStatus(Integer carId, CarStatusUpdateRequestDTO request) {
        CarInformation car = carInformationRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car", "id", carId.toString()));

        car.setCarStatus(CarInformation.CarStatus.valueOf(request.getCarStatus()));
        CarInformation updatedCar = carInformationRepository.save(car);
        return convertToDto(updatedCar);
    }

    private CarResponseDTO convertToDto(CarInformation car) {
        return new CarResponseDTO(
                car.getCarId(),
                car.getCarName(),
                car.getCarDescription(),
                car.getNumberOfDoors(),
                car.getSeatingCapacity(),
                car.getFuelType().toString(),
                car.getYear(),
                car.getManufacturer().getManufacturerName(),
                car.getManufacturer().getManufacturerCountry(),
                car.getSupplier().getSupplierName(),
                car.getCarStatus().toString(),
                car.getCarRentingPricePerDay()
        );
    }
}
