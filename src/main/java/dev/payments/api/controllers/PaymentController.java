package dev.payments.api.controllers;

import dev.payments.api.dtos.CreatePaymentDto;
import dev.payments.api.dtos.PaymentDto;
import dev.payments.api.dtos.UpdatePaymentStatusDto;
import dev.payments.api.entities.PaymentStatus;
import dev.payments.api.services.PaymentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentDto> postPayments(@RequestBody @Valid CreatePaymentDto createPaymentDto) {
        var createdPayment = paymentService.createPayment(createPaymentDto);

        return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<PaymentDto> putPayments(@RequestBody @Valid UpdatePaymentStatusDto updatePaymentStatusDto) {
        var updatedPayment = paymentService.updatePaymentStatus(updatePaymentStatusDto);

        return new ResponseEntity<>(updatedPayment, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<PaymentDto>> getPayments(
            @RequestParam(required = false) Long debitCode,
            @RequestParam(required = false) String userIdentification,
            @RequestParam(required = false) PaymentStatus status,
            Pageable pageable
    ) {
        var payments = paymentService.getPayments(debitCode, status, userIdentification, pageable);

        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable UUID id) {
        paymentService.deletePayment(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
