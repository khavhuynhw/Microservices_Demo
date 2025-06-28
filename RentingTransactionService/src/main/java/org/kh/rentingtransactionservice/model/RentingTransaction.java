package org.kh.rentingtransactionservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate; // For RentingDate

import java.util.List; // For relationship with RentingDetail

@Entity
@Table(name = "renting_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentingTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "renting_transaction_id")
    private Integer rentingTransactionId;

    @Column(name = "renting_date", nullable = false)
    private LocalDate rentingDate;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @Column(name = "renting_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RentingStatus rentingStatus;

    public enum RentingStatus {
        PENDING,
        APPROVED,
        REJECTED,
        COMPLETED
    }

    @OneToMany(mappedBy = "rentingTransaction", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RentingDetail> rentingDetails;
}
