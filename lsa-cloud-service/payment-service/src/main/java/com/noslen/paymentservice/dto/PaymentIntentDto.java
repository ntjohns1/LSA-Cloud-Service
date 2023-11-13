package com.noslen.paymentservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PaymentIntentDto {

    String id;
    Long amount;
    String currency = "usd";
    String customer;
    String description;
    String paymentMethod;
    String invoice;
    String status;

}
