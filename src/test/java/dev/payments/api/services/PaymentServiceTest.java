package dev.payments.api.services;

import com.github.javafaker.Faker;
import dev.payments.api.domain.entities.Payment;
import dev.payments.api.domain.entities.PaymentMethod;
import dev.payments.api.domain.repositories.PaymentRepository;
import dev.payments.api.domain.services.PaymentServiceImpl;
import dev.payments.api.presentation.dtos.CreatePaymentDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
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

        var paymentDto = new CreatePaymentDto(
                faker.number().randomNumber(),
                faker.number().digits(11),
                PaymentMethod.BILLET,
                null,
                BigDecimal.valueOf(faker.number().randomNumber())
        );

        var payment = new Payment(paymentDto);

        var expectedPayment = new Payment(paymentDto);
        expectedPayment.setId(randomId);

        Mockito.when(paymentRepository.save(payment)).thenReturn(expectedPayment);

        var createdPayment = paymentService.createPayment(paymentDto);

        assertThat(createdPayment.id()).isEqualTo(randomId);

        Mockito.verify(paymentRepository, Mockito.atLeastOnce()).save(payment);
    }

}
