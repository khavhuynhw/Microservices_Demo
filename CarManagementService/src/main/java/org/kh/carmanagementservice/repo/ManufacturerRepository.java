package org.kh.carmanagementservice.repo;

import org.kh.carmanagementservice.model.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, Integer> {
    Optional<Manufacturer> findByManufacturerName(String name);
}
