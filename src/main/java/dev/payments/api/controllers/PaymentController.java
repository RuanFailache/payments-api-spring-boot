package dev.payments.api.controllers;

import dev.payments.api.dtos.CreatePaymentDto;
import dev.payments.api.dtos.DeletePaymentDto;
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

@RestController
@RequestMapping("payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<?> postPayments(@RequestBody @Valid CreatePaymentDto createPaymentDto) {
        PaymentDto createdPayment = paymentService.createPayment(createPaymentDto);

        return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<?> patchPayments(@RequestBody @Valid UpdatePaymentStatusDto updatePaymentStatusDto) {
        PaymentDto updatedPayment = paymentService.updatePaymentStatus(updatePaymentStatusDto);

        return new ResponseEntity<>(updatedPayment, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getPayments(
            @RequestParam(required = false) Long debitCode,
            @RequestParam(required = false) String userIdentification,
            @RequestParam(required = false) PaymentStatus status,
            Pageable pageable
    ) {
        Page<PaymentDto> payments = paymentService.getPayments(debitCode, status, userIdentification, pageable);

        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deletePayment(@RequestBody @Valid DeletePaymentDto deletePaymentDto) {
        paymentService.deletePayment(deletePaymentDto.id());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
