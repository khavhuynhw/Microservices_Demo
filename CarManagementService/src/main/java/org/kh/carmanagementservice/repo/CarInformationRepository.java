package org.kh.carmanagementservice.repo;

import org.kh.carmanagementservice.model.CarInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarInformationRepository extends JpaRepository<CarInformation, Integer> {
    List<CarInformation> findByCarStatus(String status);
    Optional<CarInformation> findByCarIdAndCarStatus(Integer carId, String status);
}

