package org.kh.customermanagementservice.controller;

import lombok.RequiredArgsConstructor;
import org.kh.customermanagementservice.dto.CustomerProfileUpdateRequestDTO;
import org.kh.customermanagementservice.dto.CustomerRequestDTO;
import org.kh.customermanagementservice.dto.CustomerResponseDTO;
import org.kh.customermanagementservice.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class); // Add this line
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{customerId}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Integer customerId) {
        CustomerResponseDTO customer = customerService.getCustomerById(customerId);
        return ResponseEntity.ok(customer);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
        List<CustomerResponseDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable Integer id, @RequestBody CustomerRequestDTO request) {
        CustomerResponseDTO updatedCustomer = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // --- Customer Self-Management Operations ---

    @GetMapping("/me")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CustomerResponseDTO> getCurrentCustomerProfile(@RequestHeader("X-User-Id") String customerId) {
        logger.info("CustomerController: X-User-Roles header received: {}", customerId); // <-- ADD/VERIFY THIS LOG
        CustomerResponseDTO customer = customerService.findCustomerProfile(Integer.valueOf(customerId));
        return ResponseEntity.ok(customer);
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<CustomerResponseDTO> updateCurrentCustomerProfile(
            @RequestHeader("X-User-Id") String customerId,
            @RequestBody CustomerProfileUpdateRequestDTO request) {
        CustomerResponseDTO updatedCustomer = customerService.updateCustomerProfile(Integer.valueOf(customerId), request);
        return ResponseEntity.ok(updatedCustomer);
    }

    @PostMapping("/me")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<CustomerResponseDTO> createCustomer(@RequestBody CustomerRequestDTO request,
    @RequestHeader("X-User-Id") String userId, @RequestHeader("X-User-Email") String email
    ) {
        CustomerResponseDTO newCustomer = customerService.createCustomer(request, Integer.valueOf(userId), email);
        return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
    }
}
