package org.kh.carmanagementservice.service;

import org.kh.carmanagementservice.dto.CarRequestDTO;
import org.kh.carmanagementservice.dto.CarResponseDTO;
import org.kh.carmanagementservice.dto.CarStatusUpdateRequestDTO;

import java.util.List;

public interface CarInformationService {
     List<CarResponseDTO> getAllCars();
     CarResponseDTO getCarById(Integer carId);
     CarResponseDTO createCar(CarRequestDTO request);
     CarResponseDTO updateCar(Integer carId, CarRequestDTO request);
     void deleteCar(Integer carId);

     List<CarResponseDTO> findAllAvailableCars();

     CarResponseDTO updateCarStatus(Integer carId, CarStatusUpdateRequestDTO request);
}
