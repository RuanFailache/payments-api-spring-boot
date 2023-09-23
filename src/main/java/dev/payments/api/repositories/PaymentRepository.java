package dev.payments.api.repositories;

import dev.payments.api.entities.Payment;
import dev.payments.api.entities.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Page<Payment> findAllByDebitCodeOrUserIdentificationOrStatus(
            Long debitCode,
            String userIdentification,
            PaymentStatus status,
            Pageable pageable
    );
}
