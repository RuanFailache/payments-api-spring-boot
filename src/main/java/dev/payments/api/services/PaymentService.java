package dev.payments.api.services;

import java.util.*;

import dev.payments.api.dtos.CreatePaymentDto;
import dev.payments.api.dtos.PaymentDto;
import dev.payments.api.dtos.UpdatePaymentStatusDto;
import dev.payments.api.entities.Payment;
import dev.payments.api.entities.PaymentMethod;
import dev.payments.api.entities.PaymentStatus;
import dev.payments.api.repositories.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public PaymentDto createPayment(CreatePaymentDto createPaymentDto) {

        HashSet<PaymentMethod> cardRelatedMethods = new HashSet<>();
        cardRelatedMethods.add(PaymentMethod.DEBIT_CARD);
        cardRelatedMethods.add(PaymentMethod.CREDIT_CARD);

        boolean isCardNumberNullable = createPaymentDto.cardNumber() == null;
        boolean isCardRelatedMethod = cardRelatedMethods.contains(createPaymentDto.method());

        if (isCardRelatedMethod && isCardNumberNullable || !isCardRelatedMethod && !isCardNumberNullable) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Só pode haver número de cartão, caso o método de pagamento utilize cartão!"
            );
        }

        Payment payment = new Payment(createPaymentDto);
        Payment createdPayment = paymentRepository.save(payment);

        return new PaymentDto(createdPayment);

    }

    @Transactional
    public PaymentDto updatePaymentStatus(UUID paymentId, UpdatePaymentStatusDto updatePaymentStatusDto) {

        PaymentStatus paymentStatus = updatePaymentStatusDto.status();

        Payment foundPayment = findPaymentById(paymentId);
        PaymentStatus foundPaymentStatus = foundPayment.getStatus();

        if (foundPayment.isCancelled()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "O pagamento não foi encontrado!"
            );
        }

        if (foundPaymentStatus == PaymentStatus.SUCCESS) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O pagamento já foi concluido!"
            );
        }

        if (foundPaymentStatus == PaymentStatus.FAILED && paymentStatus != PaymentStatus.PENDING) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O pagamento atual falhou, logo só pode ser alterado para o status 'pendente'!"
            );
        }

        if (foundPaymentStatus == PaymentStatus.PENDING && paymentStatus == PaymentStatus.PENDING) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O pagamento com estado pendente só pode ser alterado para o status de sucesso ou de falha!"
            );
        }

        foundPayment.setStatus(paymentStatus);

        Payment updatedPayment = paymentRepository.save(foundPayment);

        return new PaymentDto(updatedPayment);

    }

    public Page<PaymentDto> getPayments(
            Long debitCode,
            PaymentStatus paymentStatus,
            String userIdentification,
            Pageable pageable
    ) {

        Payment payment = new Payment();
        payment.setDebitCode(debitCode);
        payment.setStatus(paymentStatus);
        payment.setUserIdentification(userIdentification);
        payment.setCancelled(false);

        Page<Payment> payments = paymentRepository.findAll(Example.of(payment), pageable);

        return payments.map(PaymentDto::new);

    }

    @Transactional
    public void deletePayment(UUID paymentId) {

        Payment foundPayment = findPaymentById(paymentId);

        if (foundPayment.isCancelled()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "O pagamento não foi encontrado!"
            );
        }

        if (foundPayment.getStatus() != PaymentStatus.PENDING) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O pagamento não pode ser deletado, pois não está com processamento pendente!"
            );
        }

        foundPayment.setCancelled(true);

        paymentRepository.save(foundPayment);

    }

    private Payment findPaymentById(UUID paymentId) {

        Optional<Payment> foundPaymentReference = paymentRepository.findById(paymentId);

        if (foundPaymentReference.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "O pagamento não foi encontrado!"
            );
        }

        return foundPaymentReference.get();

    }

}
