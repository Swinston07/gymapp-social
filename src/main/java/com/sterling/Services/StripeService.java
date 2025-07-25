package com.sterling.Services;

import java.util.HashMap;
import java.util.Map;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

public class StripeService {
    private static final String PRICE_ID = "price_1Rmz7BGbF0aoNlGnma1E7msm";

    public Session createSubscriptionSession (int userId, String subscriptionType, String priceId) throws StripeException {
        Stripe.apiKey = "sk_test_51RmyteGbF0aoNlGn45SzqCexqBMdAWbx3zGz5rP56KfhuatbtqUMatiXuOb5cawDQxrLJGTCBZXRAEI9mPZv2aTd00WMMG7DYY";

        Map<String, String> metadata = new HashMap<>();
        metadata.put("user_id", String.valueOf(userId));
        metadata.put("subscription_type", subscriptionType);

        // Create the checkout session
        SessionCreateParams params = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
            .setSuccessUrl("http://localhost:5173/success?session_id={CHECKOUT_SESSION_ID}")
            .setCancelUrl("http://localhost:5173/cancel")
            .addLineItem(
                SessionCreateParams.LineItem.builder()
                    .setQuantity(1L)
                    .setPrice(priceId)
                    .build()
            )
            .setSubscriptionData(
                SessionCreateParams.SubscriptionData.builder()
                    .putAllMetadata(metadata)  // ✅ attach user_id to the subscription
                    .build()
            )
            .putAllMetadata(metadata) // also attaches to session (not required but useful for checkout.session.completed)
            .build();
            
            Session session = Session.create(params);
            return session;
    }
}
