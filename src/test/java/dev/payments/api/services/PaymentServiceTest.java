package dev.payments.api.services;

import com.github.javafaker.Faker;
import dev.payments.api.domain.entities.Payment;
import dev.payments.api.domain.entities.PaymentMethod;
import dev.payments.api.domain.entities.PaymentStatus;
import dev.payments.api.domain.repositories.PaymentRepository;
import dev.payments.api.domain.services.PaymentServiceImpl;
import dev.payments.api.presentation.dtos.CreatePaymentDto;
import dev.payments.api.presentation.dtos.UpdatePaymentStatusDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    private static final Faker faker = new Faker();

    @Test
    void shouldThrowsBadRequestWhenCardNumberIsNotNullAndPaymentMethodDoesNotUseCardOnCreatePayment() {
        var payment = new CreatePaymentDto(
                faker.number().randomNumber(),
                faker.number().digits(11),
                PaymentMethod.PIX,
                faker.number().digits(16),
                BigDecimal.valueOf(faker.number().randomNumber())
        );

        try {
            paymentService.createPayment(payment);
        } catch (ResponseStatusException e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Test
    void shouldThrowsBadRequestWhenCardNumberIsNullAndPaymentMethodUsesCardOnCreatePayment() {
        var payment = new CreatePaymentDto(
                faker.number().randomNumber(),
                faker.number().digits(11),
                PaymentMethod.CREDIT_CARD,
                null,
                BigDecimal.valueOf(faker.number().randomNumber())
        );

        try {
            paymentService.createPayment(payment);
        } catch (ResponseStatusException e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Test
    void shouldReturnsCorrectlyOnCreatePayment() {
        var randomId = UUID.randomUUID();

        var dto = new CreatePaymentDto(
                faker.number().randomNumber(),
                faker.number().digits(11),
                PaymentMethod.BILLET,
                null,
                BigDecimal.valueOf(faker.number().randomNumber())
        );

        var payment = new Payment(dto);

        var expectedPayment = new Payment(dto);
        expectedPayment.setId(randomId);

        Mockito.when(paymentRepository.save(payment)).thenReturn(expectedPayment);

        var createdPayment = paymentService.createPayment(dto);

        assertThat(createdPayment.id()).isEqualTo(randomId);

        Mockito.verify(paymentRepository, Mockito.atLeastOnce()).save(payment);
    }

    @Test
    void shouldThrowsNotFoundWhenPaymentIsNotFoundOnUpdatePaymentStatus() {
        var paymentId = UUID.randomUUID();
        var dto = new UpdatePaymentStatusDto(PaymentStatus.SUCCESS);

        Mockito.when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        try {
            paymentService.updatePaymentStatus(paymentId, dto);
        } catch (ResponseStatusException e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Test
    void shouldThrowsNotFoundWhenFoundPaymentIsCancelledOnUpdatePaymentStatus() {
        var paymentId = UUID.randomUUID();
        var dto = new UpdatePaymentStatusDto(PaymentStatus.SUCCESS);

        var payment = new Payment();
        payment.setId(paymentId);
        payment.setCancelled(true);

        Mockito.when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        try {
            paymentService.updatePaymentStatus(paymentId, dto);
        } catch (ResponseStatusException e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Test
    void shouldThrowsBadRequestWhenFoundPaymentIsAlreadySuccessfulOnUpdatePaymentStatus() {
        var paymentId = UUID.randomUUID();
        var dto = new UpdatePaymentStatusDto(PaymentStatus.SUCCESS);

        var payment = new Payment();
        payment.setId(paymentId);
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setCancelled(false);

        Mockito.when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        try {
            paymentService.updatePaymentStatus(paymentId, dto);
        } catch (ResponseStatusException e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Test
    void shouldThrowsBadRequestWhenFoundPaymentIsFailedAndReceivedAnInvalidStatusOnUpdatePaymentStatus() {
        var paymentId = UUID.randomUUID();
        var dto = new UpdatePaymentStatusDto(PaymentStatus.SUCCESS);

        var payment = new Payment();
        payment.setId(paymentId);
        payment.setStatus(PaymentStatus.FAILED);
        payment.setCancelled(false);

        Mockito.when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        try {
            paymentService.updatePaymentStatus(paymentId, dto);
        } catch (ResponseStatusException e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Test
    void shouldThrowsBadRequestWhenFoundPaymentIsPendingAndReceivedAnInvalidStatusOnUpdatePaymentStatus() {
        var paymentId = UUID.randomUUID();
        var dto = new UpdatePaymentStatusDto(PaymentStatus.PENDING);

        var payment = new Payment();
        payment.setId(paymentId);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCancelled(false);

        Mockito.when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        try {
            paymentService.updatePaymentStatus(paymentId, dto);
        } catch (ResponseStatusException e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Test
    void shouldReturnsCorrectlyOnUpdatePaymentStatus() {
        var paymentId = UUID.randomUUID();
        var dto = new UpdatePaymentStatusDto(PaymentStatus.SUCCESS);

        var payment = new Payment();
        payment.setId(paymentId);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCancelled(false);

        Mockito.when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        var expectedPayment = new Payment();
        expectedPayment.setId(paymentId);
        expectedPayment.setStatus(PaymentStatus.SUCCESS);
        expectedPayment.setCancelled(false);

        Mockito.when(paymentRepository.save(expectedPayment)).thenReturn(expectedPayment);

        var updatedPayment = paymentService.updatePaymentStatus(paymentId, dto);

        assertThat(updatedPayment.status()).isEqualTo(PaymentStatus.SUCCESS);

        Mockito.verify(paymentRepository, Mockito.atLeastOnce()).save(payment);
    }

    @Test
    void shouldThrowsNotFoundWhenPaymentIsNotFoundOnDeletePayment() {
        var paymentId = UUID.randomUUID();

        Mockito.when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        try {
            paymentService.deletePayment(paymentId);
        } catch (ResponseStatusException e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Test
    void shouldThrowsNotFoundWhenFoundPaymentIsCancelledOnDeletePayment() {
        var paymentId = UUID.randomUUID();

        var payment = new Payment();
        payment.setId(paymentId);
        payment.setCancelled(true);

        Mockito.when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        try {
            paymentService.deletePayment(paymentId);
        } catch (ResponseStatusException e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Test
    void shouldThrowsBadRequestWhenFoundPaymentStatusIsNotPendingOnDeletePayment() {
        var paymentId = UUID.randomUUID();

        var payment = new Payment();
        payment.setId(paymentId);
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setCancelled(false);

        Mockito.when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        try {
            paymentService.deletePayment(paymentId);
        } catch (ResponseStatusException e) {
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Test
    void shouldReturnsCorrectlyOnDeletePayment() {
        var paymentId = UUID.randomUUID();

        var payment = new Payment();
        payment.setId(paymentId);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCancelled(false);

        Mockito.when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        var expectedPayment = new Payment();
        expectedPayment.setId(paymentId);
        expectedPayment.setStatus(PaymentStatus.PENDING);
        expectedPayment.setCancelled(true);

        Mockito.when(paymentRepository.save(expectedPayment)).thenReturn(expectedPayment);

        paymentService.deletePayment(paymentId);

        Mockito.verify(paymentRepository, Mockito.atLeastOnce()).save(payment);
    }

}
