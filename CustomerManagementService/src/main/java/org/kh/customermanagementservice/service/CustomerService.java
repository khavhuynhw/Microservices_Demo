package org.kh.customermanagementservice.service;

import org.kh.customermanagementservice.dto.CustomerProfileUpdateRequestDTO;
import org.kh.customermanagementservice.dto.CustomerRequestDTO;
import org.kh.customermanagementservice.dto.CustomerResponseDTO;
import org.kh.customermanagementservice.model.Customer;

import java.util.List;

public interface CustomerService {
    CustomerResponseDTO createCustomer(CustomerRequestDTO request,Integer userId,String email);
    CustomerResponseDTO getCustomerById(Integer customerId);
    CustomerResponseDTO updateCustomer(Integer customerId, CustomerRequestDTO request);
    void deleteCustomer(Integer customerId);
    List<CustomerResponseDTO> getAllCustomers();

    CustomerResponseDTO findCustomerProfile(Integer authenticatedCustomerId);
    CustomerResponseDTO updateCustomerProfile(Integer authenticatedCustomerId, CustomerProfileUpdateRequestDTO request);
//    CustomerResponseDTO createCustomer(CustomerProfileUpdateRequestDTO request,Integer userId,String email);
}
