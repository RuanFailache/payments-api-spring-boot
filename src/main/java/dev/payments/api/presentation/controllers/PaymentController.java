package dev.payments.api.presentation.controllers;

import dev.payments.api.presentation.dtos.*;
import dev.payments.api.domain.entities.PaymentStatus;
import dev.payments.api.presentation.services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("payments")
@Tag(name = "Rotas de pagamentos")
public class PaymentController {

    private final PaymentService paymentService;


    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @PostMapping
    @Operation(summary = "Rota para criar um pagamento")
    @ApiResponse(responseCode = "201", description = "Pagamento criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Os dados fornecidos são inválidos")
    public ResponseEntity<PaymentDto> postPayments(@RequestBody @Valid CreatePaymentDto createPaymentDto) {

        var createdPayment = paymentService.createPayment(createPaymentDto);

        return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);

    }


    @PutMapping("{id}")
    @Operation(summary = "Rota para atualizar o status de um pagamento")
    @ApiResponse(responseCode = "200", description = "Pagamento atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Pagamento com status fornecido inválido")
    @ApiResponse(responseCode = "404", description = "Pagamento não encontrado")
    public ResponseEntity<PaymentDto> putPayments(
            @PathVariable UUID id,
            @RequestBody @Valid UpdatePaymentStatusDto updatePaymentStatusDto
    ) {

        var updatedPayment = paymentService.updatePaymentStatus(id, updatePaymentStatusDto);

        return new ResponseEntity<>(updatedPayment, HttpStatus.OK);

    }


    @GetMapping
    @Operation(summary = "Rota para listar todos os pagamentos")
    @ApiResponse(responseCode = "200", description = "Pagamentos listados com sucesso")
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
    @Operation(summary = "Rota para excluir um pagamento")
    @ApiResponse(responseCode = "204", description = "Pagamento cancelado com sucesso")
    @ApiResponse(responseCode = "400", description = "Pagamento já havia sido concluido")
    @ApiResponse(responseCode = "404", description = "Pagamento não encontrado")
    public ResponseEntity<?> deletePayment(@PathVariable UUID id) {

        paymentService.deletePayment(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
