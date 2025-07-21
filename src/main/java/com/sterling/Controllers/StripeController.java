package com.sterling.Controllers;

import java.util.Collections;

import com.sterling.Services.StripeService;
import com.stripe.exception.StripeException;

import io.javalin.http.Context;

public class StripeController {
    private static final StripeService stripeService = new StripeService();

    public static void createCheckoutSession(Context ctx) {
        try {
            String sessionId = stripeService.createSubscriptionSession();
            ctx.json(Collections.singletonMap("id", sessionId));
        } catch (StripeException e) {
            e.printStackTrace();
            ctx.status(500).json(Collections.singletonMap("error", e.getMessage()));
        }
    }
}
