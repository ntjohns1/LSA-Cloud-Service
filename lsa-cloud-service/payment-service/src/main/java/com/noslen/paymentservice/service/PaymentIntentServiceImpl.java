package com.noslen.paymentservice.service;

import com.noslen.paymentservice.dto.PaymentIntentDto;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeCollection;
import com.stripe.model.StripeSearchResult;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentIntentSearchParams;
import com.stripe.param.PaymentIntentUpdateParams;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PaymentIntentServiceImpl implements PaymentIntentService {

    private final StripeClient stripeClient;

    private final StripeClientHelper stripeClientHelper;


    public PaymentIntentServiceImpl(StripeClient stripeClient, StripeClientHelper stripeClientHelper) {
        this.stripeClient = stripeClient;
        this.stripeClientHelper = stripeClientHelper;
    }

    @Override
    public Mono<StripeCollection<PaymentIntent>> listAllPaymentIntents() {
        return stripeClientHelper.executeStripeCall(() -> stripeClient.paymentIntents().list());
    }

    @Override
    public Mono<PaymentIntent> retrievePaymentIntent(String id) {
        return Mono.fromCallable(() -> stripeClient.paymentIntents()
                        .retrieve(id))
                .onErrorMap(StripeException.class,
                            e -> new Exception("Error processing Stripe API",
                                               e));
    }

    @Override
    public Mono<StripeSearchResult<PaymentIntent>> searchPaymentIntentByCustomer(String customerId) {
        return Mono.fromCallable(() -> stripeClient.paymentIntents()
                        .search(PaymentIntentSearchParams.builder()
                                        .setQuery(String.format("customer:%s",
                                                                customerId))
                                        .build()))
                .onErrorMap(StripeException.class,
                            e -> new Exception("Error processing Stripe API",
                                               e));
    }

    @Override
    public Mono<PaymentIntent> createPaymentIntent(PaymentIntentDto paymentIntentDto) {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(paymentIntentDto.getAmount())
                .setAutomaticPaymentMethods(PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                                    .setEnabled(true)
                                                    .build())
                .setCustomer(paymentIntentDto.getCustomer())
                .setCurrency("usd")

                .build();
        return Mono.fromCallable(() -> stripeClient.paymentIntents()
                        .create(params))
                .onErrorMap(StripeException.class,
                            e -> new Exception("Error processing Stripe API",
                                               e));
    }

    @Override
    public Mono<Void> updatePaymentIntent(String id, PaymentIntentDto paymentIntentDto) {
        PaymentIntentUpdateParams params = PaymentIntentUpdateParams.builder()
                .setAmount(paymentIntentDto.getAmount())
                .setCustomer(paymentIntentDto.getCustomer())
                .setDescription(paymentIntentDto.getDescription())
                .setPaymentMethod(paymentIntentDto.getPaymentMethod())
                .build();
        return Mono.fromRunnable(() -> {
                    try {
                        stripeClient.paymentIntents()
                                .update(id,
                                        params);
                    } catch (StripeException e) {
                        throw new RuntimeException(e);
                    }
                })
                .onErrorMap(ex -> {
                    if (ex.getCause() instanceof StripeException) {
                        return new Exception("Error processing Stripe API",
                                             ex.getCause());
                    }
                    return ex;
                })
                .then();
    }

    @Override
    public Mono<PaymentIntent> capturePaymentIntent(String id) {
        return Mono.fromCallable(() -> stripeClient.paymentIntents()
                        .capture(id))
                .onErrorMap(StripeException.class,
                            e -> new Exception("Error processing Stripe API",
                                               e));
    }

    @Override
    public Mono<Void> cancelPaymentIntent(String id) {
        return Mono.fromRunnable(() -> {
                    try {
                        stripeClient.paymentIntents()
                                .cancel(id);
                    } catch (StripeException e) {
                        throw new RuntimeException(e);
                    }
                })
                .onErrorMap(ex -> {
                    if (ex.getCause() instanceof StripeException) {
                        return new Exception("Error processing Stripe API",
                                             ex.getCause());
                    }
                    return ex;
                })
                .then();
    }
}
