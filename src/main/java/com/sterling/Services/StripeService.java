package com.sterling.Services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sterling.DAO.UserDAO;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Subscription;
import com.stripe.model.SubscriptionCollection;
import com.stripe.model.checkout.Session;
import com.stripe.param.SubscriptionListParams;
import com.stripe.param.checkout.SessionCreateParams;

public class StripeService {
    //private static final String PRICE_ID = "price_1Rmz7BGbF0aoNlGnma1E7msm";

    public Session createSubscriptionSession (int userId, String subscriptionType, String priceId) throws StripeException {
        Stripe.apiKey = "sk_test_51RmyteGbF0aoNlGn45SzqCexqBMdAWbx3zGz5rP56KfhuatbtqUMatiXuOb5cawDQxrLJGTCBZXRAEI9mPZv2aTd00WMMG7DYY";

        Map<String, String> metadata = new HashMap<>();
        metadata.put("user_id", String.valueOf(userId));
        metadata.put("subscription_type", subscriptionType);

        // 🧠 Step 2: Get the existing Stripe customer ID from DB
        UserDAO userDao = new UserDAO();
        UserService userService = new UserService(userDao);
        String existingCustomerId = userService.getStripeCustomerIdByUserId(userId);

        // Create the checkout session
        SessionCreateParams.Builder builder = SessionCreateParams.builder()
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
            .putAllMetadata(metadata); // also attaches to session (not required but useful for checkout.session.completed)

        // 🧩 Reuse existing customer ID if available
        if (existingCustomerId != null && !existingCustomerId.isEmpty()) {
            builder.setCustomer(existingCustomerId);
        }
            
        Session session = Session.create(builder.build());
        return session;
    }

    public com.stripe.model.billingportal.Session createBillingPortalSession(String customerId, String returnUrl) throws StripeException {
        Stripe.apiKey = "sk_test_51RmyteGbF0aoNlGn45SzqCexqBMdAWbx3zGz5rP56KfhuatbtqUMatiXuOb5cawDQxrLJGTCBZXRAEI9mPZv2aTd00WMMG7DYY";

        Map<String, Object> params = new HashMap<>();

        params.put("customer", customerId);
        params.put("return_url", returnUrl);

        return com.stripe.model.billingportal.Session.create(params);
    }

    public List<Subscription> getAllSubscriptions(String customerId) throws StripeException {
        Stripe.apiKey = "sk_test_51RmyteGbF0aoNlGn45SzqCexqBMdAWbx3zGz5rP56KfhuatbtqUMatiXuOb5cawDQxrLJGTCBZXRAEI9mPZv2aTd00WMMG7DYY";

        SubscriptionListParams params = SubscriptionListParams.builder()
            .setCustomer(customerId)
            .setStatus(SubscriptionListParams.Status.ACTIVE)
            .setLimit(10L)
            .build();

        SubscriptionCollection subscriptions = Subscription.list(params);
        return subscriptions.getData();
    } 

    public Subscription cancelSubscription(String subscriptionId) throws StripeException {
        Stripe.apiKey = "sk_test_51RmyteGbF0aoNlGn45SzqCexqBMdAWbx3zGz5rP56KfhuatbtqUMatiXuOb5cawDQxrLJGTCBZXRAEI9mPZv2aTd00WMMG7DYY";

        return Subscription.retrieve(subscriptionId).cancel();
    }
}
