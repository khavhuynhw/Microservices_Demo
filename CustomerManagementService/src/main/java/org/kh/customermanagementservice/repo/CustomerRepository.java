package org.kh.customermanagementservice.repo;

import org.kh.customermanagementservice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    // This interface will automatically provide CRUD operations for Customer entities
    // No additional methods are needed unless custom queries are required
    Optional<Customer> findByEmail(String email);
    boolean existsByUserId(Integer userId);
}
