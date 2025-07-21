package com.sterling.Services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

public class StripeService {
    private static final String PRICE_ID = "price_1Rmz7BGbF0aoNlGnma1E7msm";

    public String createSubscriptionSession () throws StripeException {
        Stripe.apiKey = "sk_test_51RmyteGbF0aoNlGn45SzqCexqBMdAWbx3zGz5rP56KfhuatbtqUMatiXuOb5cawDQxrLJGTCBZXRAEI9mPZv2aTd00WMMG7DYY";

        SessionCreateParams params =
            SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSuccessUrl("http://localhost:3000/success?session_id={CHECKOUT_SESSION}")
                .setCancelUrl("http://localhost:3000/cancel")
                .addLineItem(
                    SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPrice(PRICE_ID)
                        .build())
                    .build();
            Session session = Session.create(params);
            return session.getId();
    }
}
