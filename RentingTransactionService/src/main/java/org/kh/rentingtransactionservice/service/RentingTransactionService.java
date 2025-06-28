package org.kh.rentingtransactionservice.service;

import org.kh.rentingtransactionservice.dto.*;
import org.kh.rentingtransactionservice.model.RentingDetail;
import org.kh.rentingtransactionservice.model.RentingTransaction;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

public interface RentingTransactionService {
    RentingTransactionResponseDTO createTransaction(RentingDetailCreateDTO request,String token);
    List<RentingTransactionResponseDTO> getTransactionByCustomerId(Integer id);
    RentingTransactionResponseDTO getTransactionById(Integer transactionId);
    List<RentingTransactionResponseDTO> findAllTransactions();
    RentingTransactionResponseDTO updateTransactionStatus(Integer transactionId, RentingStatusUpdateRequestDTO request,String token);

    RentingTransactionResponseDTO updateTransaction(Integer transactionId, RentingDetailCreateDTO request,String token);

    List<RentingTransactionResponseDTO> getRentingTransactionByPeriod(LocalDate startDate, LocalDate endDate);
}
