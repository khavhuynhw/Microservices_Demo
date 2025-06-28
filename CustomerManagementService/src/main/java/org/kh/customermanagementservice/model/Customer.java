package org.kh.customermanagementservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate; // For storing date of birth

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "user_id", unique = true, nullable = false)
    private  Integer userId;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "telephone", unique = true, nullable = false)
    private String telephone;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "customer_birthday")
    private LocalDate customerBirthday;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CustomerStatus customerStatus;

    public enum CustomerStatus {
        ACTIVE,
        INACTIVE,
        SUSPENDED
    }

}
