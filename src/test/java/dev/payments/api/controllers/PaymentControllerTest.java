package dev.payments.api.controllers;

import com.github.javafaker.Faker;
import dev.payments.api.presentation.dtos.CreatePaymentDto;
import dev.payments.api.presentation.dtos.UpdatePaymentStatusDto;
import dev.payments.api.domain.entities.PaymentMethod;
import dev.payments.api.domain.entities.PaymentStatus;
import dev.payments.api.presentation.services.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class PaymentControllerTest {

    private final Faker faker = new Faker(new Locale("pt-BR"));

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<CreatePaymentDto> createPaymentDtoJson;

    @Autowired
    private JacksonTester<UpdatePaymentStatusDto> updatePaymentDtoJson;

    @MockBean
    private PaymentService paymentService;

    @Test
    void shouldThrowBadRequestOnInvalidDataAtPostPayments() throws Exception {
        var request = MockMvcRequestBuilders.post("/payments");

        var response = mvc.perform(request).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldReturnsCorrectlyOnSuccessfulPostPayments() throws Exception {
        var body = new CreatePaymentDto(
                faker.number().randomNumber(),
                faker.number().digits(11),
                PaymentMethod.PIX,
                null,
                BigDecimal.valueOf(faker.number().randomNumber())
        );

        var request = MockMvcRequestBuilders.post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createPaymentDtoJson.write(body).getJson());

        var response = mvc.perform(request).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void shouldThrowsBadRequestOnInvalidDataAtPutPayments() throws Exception {
        var paymentId = faker.internet().uuid();

        var path = String.format("/payments/%s", paymentId);

        var request = MockMvcRequestBuilders.put(path);

        var response = mvc.perform(request).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldReturnsCorrectlyOnSuccessfulPutPayments() throws Exception {
        var paymentId = faker.internet().uuid();

        var path = String.format("/payments/%s", paymentId);

        var body = new UpdatePaymentStatusDto(PaymentStatus.SUCCESS);

        var request = MockMvcRequestBuilders.put(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatePaymentDtoJson.write(body).getJson());

        var response = mvc.perform(request).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void shouldReturnsCorrectlyOnGetPayments() throws Exception {
        var request = MockMvcRequestBuilders.get("/payments");

        var response = mvc.perform(request).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void shouldReturnsCorrectlyOnSuccessfulDeletePayments() throws Exception {
        var paymentId = faker.internet().uuid();

        var path = String.format("/payments/%s", paymentId);

        var request = MockMvcRequestBuilders.delete(path);

        var response = mvc.perform(request).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}
