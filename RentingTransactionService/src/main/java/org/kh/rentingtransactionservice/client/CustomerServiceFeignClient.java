package org.kh.rentingtransactionservice.client;

import org.kh.rentingtransactionservice.dto.CustomerResponseFromCustomerServiceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "customer-management-service", path = "/api/v1/customers")
public interface CustomerServiceFeignClient {

    @GetMapping("/{id}")
    CustomerResponseFromCustomerServiceDTO getCustomerById(@PathVariable("id") Integer customerId);
} 