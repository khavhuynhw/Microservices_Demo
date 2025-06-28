package org.kh.rentingtransactionservice.repo;

import org.kh.rentingtransactionservice.model.RentingTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RentingTransactionRepository extends JpaRepository<RentingTransaction, Integer> {
    List<RentingTransaction> findByCustomerIdOrderByRentingDateDesc(Integer customerId);
    List<RentingTransaction> findByCustomerId(Integer customerId);
    // For reporting purposes (admin)
    List<RentingTransaction> findByRentingDateBetweenOrderByRentingDateDesc(LocalDate startDate, LocalDate endDate);
}