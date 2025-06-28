package org.kh.customermanagementservice.service.Impl;

import lombok.RequiredArgsConstructor;
import org.kh.customermanagementservice.dto.CustomerProfileUpdateRequestDTO;
import org.kh.customermanagementservice.dto.CustomerRequestDTO;
import org.kh.customermanagementservice.dto.CustomerResponseDTO;
import org.kh.customermanagementservice.exception.ResourceNotFoundException;
import org.kh.customermanagementservice.model.Customer;
import org.kh.customermanagementservice.repo.CustomerRepository;
import org.kh.customermanagementservice.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    // --- Admin Operations ---
    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepository.findAll().stream().map(this::convertToDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDTO getCustomerById(Integer customerId) {
        return customerRepository.findById(customerId)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
    }


    @Override
    public CustomerResponseDTO updateCustomer(Integer customerId, CustomerRequestDTO request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId.toString()));

        customer.setCustomerName(request.getCustomerName());
        customer.setTelephone(request.getTelephone());
        customer.setEmail(request.getEmail());
        customer.setCustomerBirthday(request.getCustomerBirthday());
        customer.setCustomerStatus(Customer.CustomerStatus.valueOf(request.getCustomerStatus().toUpperCase()));

        Customer updatedCustomer = customerRepository.save(customer);
        return convertToDto(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Integer customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new RuntimeException("Customer not found with ID: " + customerId);
        }
        Customer customer = customerRepository.findById(customerId).get();
        customer.setCustomerStatus(Customer.CustomerStatus.INACTIVE);
        customerRepository.save(customer);
    }


    // --- Customer Operations ---

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDTO findCustomerProfile(Integer authenticatedCustomerId) {
        Customer customer = customerRepository.findById(authenticatedCustomerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found for ID", "id", authenticatedCustomerId.toString()));
        return convertToDto(customer);
    }

    @Override
    @Transactional
    public CustomerResponseDTO updateCustomerProfile(Integer authenticatedCustomerId, CustomerProfileUpdateRequestDTO request) {
        Customer customer = customerRepository.findById(authenticatedCustomerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found for ID", "id", authenticatedCustomerId.toString()));
        customer.setCustomerId(authenticatedCustomerId);
        customer.setCustomerName(request.getCustomerName());
        customer.setTelephone(request.getTelephone());
        customer.setCustomerBirthday(request.getCustomerBirthday());

        Customer updatedCustomer = customerRepository.save(customer);
        return convertToDto(updatedCustomer);
    }

    @Override
    @Transactional
    public CustomerResponseDTO createCustomer(CustomerRequestDTO request,Integer userId,String email) {
        if (userId == null || userId.describeConstable().isEmpty()) {
            throw new RuntimeException("Customer ID cannot be null or empty");
        }
        if (customerRepository.existsById(userId)) {
            throw new IllegalArgumentException("Customer with ID " + userId + " already exists.");
        }

        if (customerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Customer with email " + request.getEmail() + " already exists.");
        }
        Customer customer = new Customer();
        customer.setCustomerBirthday(request.getCustomerBirthday());
        customer.setUserId(userId);
        customer.setCustomerName(request.getCustomerName());
        customer.setTelephone(request.getTelephone());
        customer.setEmail(email);
        customer.setCustomerStatus(Customer.CustomerStatus.ACTIVE);
        customerRepository.save(customer);
        return convertToDto(customer);
    }

    private CustomerResponseDTO convertToDto(Customer customer) {
        return new CustomerResponseDTO(
                customer.getCustomerId(),
                customer.getCustomerName(),
                customer.getTelephone(),
                customer.getEmail(),
                customer.getCustomerBirthday(),
                customer.getCustomerStatus().toString()
        );
    }
}
