package org.kh.rentingtransactionservice.repo;

import org.kh.rentingtransactionservice.model.RentingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RentingDetailRepository extends JpaRepository<RentingDetail, Integer> {
    List<RentingDetail> findByCarId(Integer carId);
    List<RentingDetail> findByStartDateGreaterThanEqualAndEndDateLessThanEqualOrderByStartDateDesc(
            LocalDate startDate, LocalDate endDate);
}