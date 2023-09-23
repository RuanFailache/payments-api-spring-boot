package dev.payments.api.services;

import java.util.*;
import dev.payments.api.dtos.CreatePaymentDto;
import dev.payments.api.dtos.UpdatePaymentStatusDto;
import dev.payments.api.entities.Payment;
import dev.payments.api.entities.PaymentMethod;
import dev.payments.api.entities.PaymentStatus;
import dev.payments.api.repositories.PaymentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment createPayment(CreatePaymentDto createPaymentDto) {

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

        return paymentRepository.save(payment);

    }

    public Payment updatePaymentStatus(UpdatePaymentStatusDto updatePaymentStatusDto) {

        UUID paymentId = updatePaymentStatusDto.id();
        PaymentStatus paymentStatus = updatePaymentStatusDto.status();

        Optional<Payment> foundPaymentReference = paymentRepository.findById(paymentId);

        if (foundPaymentReference.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "O pagamento não foi encontrado!"
            );
        }

        Payment foundPayment = foundPaymentReference.get();
        PaymentStatus foundPaymentStatus = foundPayment.getStatus();

        if (foundPaymentStatus == PaymentStatus.SUCCESS) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "O pagamento já foi concluido!"
            );
        }

        if (foundPaymentStatus == PaymentStatus.FAILED && paymentStatus != PaymentStatus.PENDING) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "O pagamento atual falhou, logo só pode ser alterado para o status 'pendente'!"
            );
        }

        if (foundPaymentStatus == PaymentStatus.PENDING && paymentStatus == PaymentStatus.PENDING) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "O pagamento com estado pendente só pode ser alterado para o status de sucesso ou de falha!"
            );
        }

        foundPayment.setStatus(paymentStatus);

        return paymentRepository.save(foundPayment);

    }

}
