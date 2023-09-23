package dev.payments.api.services;

import java.util.*;
import dev.payments.api.dtos.CreatePaymentDto;
import dev.payments.api.entities.Payment;
import dev.payments.api.entities.PaymentMethod;
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

    public Payment createPayment(CreatePaymentDto paymentDto) {

        HashSet<PaymentMethod> cardRelatedMethods = new HashSet<>();
        cardRelatedMethods.add(PaymentMethod.cartao_debito);
        cardRelatedMethods.add(PaymentMethod.cartao_credito);

        boolean isCardNumberNullable = paymentDto.cardNumber() == null;
        boolean isCardRelatedMethod = cardRelatedMethods.contains(paymentDto.method());

        if (isCardRelatedMethod && isCardNumberNullable || !isCardRelatedMethod && !isCardNumberNullable) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Só pode haver número de cartão, caso o método de pagamento utilize cartão!"
            );
        }

        Payment payment = new Payment(paymentDto);

        return paymentRepository.save(payment);

    }

}
