package dev.payments.api.controllers;

import dev.payments.api.dtos.CreatePaymentDto;
import dev.payments.api.dtos.PaymentDto;
import dev.payments.api.dtos.UpdatePaymentStatusDto;
import dev.payments.api.entities.Payment;
import dev.payments.api.services.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentDto> postPayments(@RequestBody @Valid CreatePaymentDto createPaymentDto) {
        PaymentDto createdPayment = paymentService.createPayment(createPaymentDto);

        return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<PaymentDto> putPayments(@RequestBody @Valid UpdatePaymentStatusDto updatePaymentStatusDto) {
        PaymentDto updatedPayment = paymentService.updatePaymentStatus(updatePaymentStatusDto);

        return new ResponseEntity<>(updatedPayment, HttpStatus.CREATED);
    }

}
