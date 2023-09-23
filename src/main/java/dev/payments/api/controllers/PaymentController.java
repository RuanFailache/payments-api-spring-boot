package dev.payments.api.controllers;

import dev.payments.api.dtos.CreatePaymentDto;
import dev.payments.api.entities.Payment;
import dev.payments.api.repositories.PaymentRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("payments")
public class PaymentController {

    private final PaymentRepository paymentRepository;

    public PaymentController(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @PostMapping
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public String registerPayment(@RequestBody @Valid CreatePaymentDto createPaymentDto) {
        Payment payment = new Payment(createPaymentDto);

        Payment createdPayment = paymentRepository.save(payment);

        return createdPayment.getId().toString();
    }

}
