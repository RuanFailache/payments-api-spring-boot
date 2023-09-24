package dev.payments.api.presentation.services;

import dev.payments.api.presentation.dtos.CreatePaymentDto;
import dev.payments.api.presentation.dtos.PaymentDto;
import dev.payments.api.presentation.dtos.UpdatePaymentStatusDto;
import dev.payments.api.domain.entities.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PaymentService {
    PaymentDto createPayment(CreatePaymentDto createPaymentDto);

    PaymentDto updatePaymentStatus(
            UUID paymentId,
            UpdatePaymentStatusDto updatePaymentStatusDto
    );

    Page<PaymentDto> getPayments(
            Long debitCode,
            PaymentStatus paymentStatus,
            String userIdentification,
            Pageable pageable
    );

    void deletePayment(UUID paymentId);
}
