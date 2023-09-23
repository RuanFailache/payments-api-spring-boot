package dev.payments.api.entities;

import dev.payments.api.dtos.CreatePaymentDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;


@Table(name = "payments")
@Entity(name = "Payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "debit_code", nullable = false)
    private Long debitCode;

    @Column(name = "user_identification", nullable = false)
    private String userIdentification;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "payment_value")
    private BigDecimal paymentValue;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    public Payment(CreatePaymentDto createPaymentDto) {
        this.cardNumber = createPaymentDto.cardNumber();
        this.debitCode = createPaymentDto.debitCode();
        this.method = createPaymentDto.method();
        this.userIdentification = createPaymentDto.userIdentification();
        this.paymentValue = createPaymentDto.paymentValue();
    }

}
