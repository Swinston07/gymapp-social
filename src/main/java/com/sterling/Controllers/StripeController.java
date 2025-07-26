package com.sterling.Controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.sterling.DAO.UserDAO;
import com.sterling.Services.StripeService;
import com.sterling.Services.UserService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.Invoice;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.javalin.http.Context;

public class StripeController {
    private static final StripeService stripeService = new StripeService();
    private static final String endpointSecret = "whsec_781567d92026fd9b0f0a281aefb349e7ae54a1870c1f707c934415dbd15e8d9c";

    // ✅ Webhook Handler
    public static void handleWebhook(Context ctx) {
        String payload = ctx.body();
        String sigHeader = ctx.header("Stripe-Signature");

        System.out.println("\n\n🎯 ===============================");
        System.out.println("📡  Incoming Stripe Webhook...");
        System.out.println("🎯 ===============================");

        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            System.err.println("❌ [Webhook] Signature verification failed!");
            ctx.status(400).result("Invalid signature");
            return;
        }

        System.out.println("📨 [Stripe] Event Type: " + event.getType());

        switch (event.getType()) {
            case "checkout.session.completed":
                handleCheckoutCompleted(event);
                break;
            case "invoice.payment_succeeded":
                handleInvoicePaid(event);
                break;
            case "customer.subscription.deleted":
                handleSubscriptionCancelled(event);
                break;
            default:
                break;
        }

        ctx.status(200);
    }

    // ✅ Logic for handling subscription success
    private static void handleCheckoutCompleted(Event event) {
        try {
            String rawJson = event.getDataObjectDeserializer().getRawJson();
            if (rawJson == null || rawJson.isBlank()) {
                System.err.println("❌ Raw JSON is empty");
                return;
            }

            JsonObject json = JsonParser.parseString(rawJson).getAsJsonObject();
            String sessionId = json.get("id").getAsString();

            Session session = Session.retrieve(sessionId);  // ✅ Manually retrieve full session

            Map<String, String> metadata = session.getMetadata();
            String userIdStr = metadata.get("user_id");
            String type = metadata.get("subscription_type");
            String customerId = session.getCustomer();  // ✅ Stripe customer ID

            if (userIdStr == null || type == null || customerId == null) {
                System.err.println("❌ Missing metadata or customer ID.");
                return;
            }

            int userId = Integer.parseInt(userIdStr);
            UserDAO userDao = new UserDAO();
            UserService userService = new UserService(userDao);

            // ✅ Set Stripe Customer ID
            userService.updateStripeCustomerId(userId, customerId);

            if ("premium".equalsIgnoreCase(type)) {
                boolean updated = userService.updatePremiumStatus(userId, true);
                System.out.println(updated ? "✅ User upgraded to PREMIUM" : "⚠️ Failed to upgrade user to PREMIUM");
            } else if ("trainer".equalsIgnoreCase(type)) {
                boolean updated = userService.updateUserRole(userId, "trainer");
                System.out.println(updated ? "✅ User upgraded to TRAINER" : "⚠️ Failed to upgrade user to TRAINER");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void handleInvoicePaid(Event event) {
        try {
            String rawJson = event.getDataObjectDeserializer().getRawJson();
            if (rawJson == null || rawJson.isBlank()) return;

            JsonObject json = JsonParser.parseString(rawJson).getAsJsonObject();
            String invoiceId = json.get("id").getAsString();
            Invoice invoice = Invoice.retrieve(invoiceId);
            Subscription subscription = Subscription.retrieve(invoice.getSubscription());

            String userIdStr = subscription.getMetadata().get("user_id");
            String type = subscription.getMetadata().get("subscription_type");

            if (userIdStr == null || type == null) return;

            int userId = Integer.parseInt(userIdStr);
            UserDAO userDao = new UserDAO();
            UserService userService = new UserService(userDao);

            if ("premium".equalsIgnoreCase(type)) {
                boolean updated = userService.updatePremiumStatus(userId, true);
                System.out.print("==============================");
                System.out.print("userId: " + userId);
                System.out.print("updated: " + updated);
                System.out.print("==============================");


                System.out.println(updated ? "✅ Invoice → Upgraded to PREMIUM" : "⚠️ Invoice → Failed to upgrade to PREMIUM");
            } else if ("trainer".equalsIgnoreCase(type)) {
                boolean updated = userService.updateUserRole(userId, "trainer");
                System.out.println(updated ? "✅ Invoice → Upgraded to TRAINER" : "⚠️ Invoice → Failed to upgrade to TRAINER");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleSubscriptionCancelled(Event event) {
        try {
            String rawJson = event.getDataObjectDeserializer().getRawJson();
            if (rawJson == null || rawJson.isBlank()) return;

            JsonObject json = JsonParser.parseString(rawJson).getAsJsonObject();
            String subscriptionId = json.get("id").getAsString();

            // Get the full Subscription object from Stripe
            Subscription subscription = Subscription.retrieve(subscriptionId);

            String userIdStr = subscription.getMetadata().get("user_id");
            String type = subscription.getMetadata().get("subscription_type");

            if (userIdStr == null || type == null) return;

            int userId = Integer.parseInt(userIdStr);
            UserDAO userDao = new UserDAO();
            UserService userService = new UserService(userDao);

            switch (type.toLowerCase()) {
                case "premium":
                    boolean downgraded = userService.updatePremiumStatus(userId, false);
                    System.out.println(downgraded
                        ? "✅ Subscription cancelled → Premium removed"
                        : "⚠️ Failed to downgrade Premium");
                    break;

                case "trainer":
                    boolean roleRevoked = userService.updateUserRole(userId, "client");
                    System.out.println(roleRevoked
                        ? "✅ Subscription cancelled → Trainer role revoked"
                        : "⚠️ Failed to revoke Trainer role");
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ✅ Checkout Session Creation
    public static void createCheckoutSession(Context ctx) {
        try {
            int userId = ctx.attribute("userId");

            // 🆕 Parse JSON body
            String subscriptionType = ctx.bodyAsClass(Map.class).get("type").toString();
            String priceId = ctx.bodyAsClass(Map.class).get("priceId").toString();

            Session session = stripeService.createSubscriptionSession(userId, subscriptionType, priceId);

            System.out.println("\n\n💳 ===============================");
            System.out.println("🚀  Creating Stripe Checkout Session");
            System.out.println("👤  User ID: " + userId);
            System.out.println("🔖  Subscription Type: " + subscriptionType);
            System.out.println("💰  Price ID: " + priceId);
            System.out.println("💳  Session URL: " + session.getUrl());
            System.out.println("💳 ===============================");

            Map<String, String> response = new HashMap<>();
            response.put("id", session.getId());
            response.put("url", session.getUrl());

            ctx.json(response);
        } catch (StripeException e) {
            e.printStackTrace();
            ctx.status(500).json(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(400).json(Collections.singletonMap("error", "Invalid request body"));
        }
    }

    public static void createBillingPortal(Context ctx) {
        try {
            int userId = ctx.attribute("userId");
            UserDAO userDao = new UserDAO();
            UserService userService = new UserService(userDao);
            String customerId = userService.getStripeCustomerIdByUserId(userId);

            if(customerId == null || customerId.isEmpty()) {
                ctx.status(400).json(Collections.singletonMap("error", "No stripe customer ID found"));
                return;
            }

            String returnUrl = "http://localhost:5173/profile/" + userId;

            var session = stripeService.createBillingPortalSession(customerId, returnUrl);

            Map<String, String> response = new HashMap<>();
            response.put("url", session.getUrl());

            ctx.json(response);
        } catch (StripeException e) {
            e.printStackTrace();
            ctx.status(500).json(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(400).json(Collections.singletonMap("error", "Invalid request"));
        }
    }
}
