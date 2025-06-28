
package org.kh.rentingtransactionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class CarResponseFromCarServiceDTO {
    private Integer carId;
    private String carName;
    private String carDescription;
    private Integer numberOfDoors;
    private Integer seatingCapacity;
    private String fuelType;
    private Integer year;
    private String manufacturerName;
    private String manufacturerCountry;
    private String supplierName;
    private String carStatus;
    private Double carRentingPricePerDay;
}