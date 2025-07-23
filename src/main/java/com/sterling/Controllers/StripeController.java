package com.sterling.Controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.sterling.DAO.UserDAO;
import com.sterling.Models.User;
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
            case "checout.session.completed":
                handleCheckoutCompleted(event);
                break;
            case "invoice.payment_succeeded":
                handleInvoicePaid(event);
                break;
            default:
                break;
        }

        ctx.status(200);
    }

    // ✅ Logic for handling subscription success
    private static void handleCheckoutCompleted(Event event) {
        Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);

        if (session == null) {
            System.err.println("❌ [Webhook] Session deserialization failed.");
            return;
        }

        System.out.println("📦 [Webhook] Session ID: " + session.getId());
        System.out.println("📦 [Webhook] Metadata: " + session.getMetadata());

        Map<String, String> metadata = session.getMetadata();
        if (metadata != null && metadata.containsKey("user_id")) {
            String userIdStr = metadata.get("user_id");
            System.out.println("🔍 [Webhook] Extracted user_id: " + userIdStr);

            try {
                int userId = Integer.parseInt(userIdStr);
                UserDAO userDao = new UserDAO();
                UserService userService = new UserService(userDao);
                User user = userService.getUserById(userId);

                if (user != null) {
                    System.out.println("👤 [Webhook] Found user: " + user.getUsername());
                    boolean updated = userService.updateUserRole(userId, "trainer");

                    if (updated) {
                        System.out.println("✅ [Webhook] User " + user.getUsername() + " upgraded to TRAINER.");
                    } else {
                        System.err.println("⚠️  [Webhook] Failed to update user role.");
                    }
                } else {
                    System.err.println("❌ [Webhook] No user found with ID: " + userId);
                }

            } catch (NumberFormatException e) {
                System.err.println("❌ [Webhook] Invalid user ID format: " + userIdStr);
            } catch (Exception e) {
                System.err.println("❌ [Webhook] Error processing: " + e.getMessage());
                e.printStackTrace();
            }

        } else {
            System.err.println("⚠️  [Webhook] Missing user_id in metadata.");
        }
    }

    public static void handleInvoicePaid(Event event) {
        System.out.println("============================");
        System.out.println("Handling invoice_payment.paid");
        System.out.println("============================");
        
        try {
            String rawJson = event.getDataObjectDeserializer()
                                .getRawJson();
            
            if (rawJson == null || rawJson.isBlank()) {
                System.err.println("❌ [Webhook] Raw JSON is null.");
                return;
        }

            JsonObject json = JsonParser.parseString(rawJson).getAsJsonObject();
            String invoiceId = json.get("id").getAsString();
            System.out.println("📦 Raw Invoice Payload: " + invoiceId);
            System.out.println("🔍 Retrieving invoice by ID: " + invoiceId);
            Invoice invoice = Invoice.retrieve(invoiceId);
            //String customerId = invoice.getCustomer(); //Stripe customer id
            String subscriptionId = invoice.getSubscription();

            if(subscriptionId == null) {
                System.err.println("❌ Subscription ID is null in the invoice.");
                return;
            }

            System.out.println("====================================");
            System.out.println("✅ Subscription ID: " + subscriptionId);
            System.out.println("====================================");

            Subscription subscription = Subscription.retrieve(invoice.getSubscription());

            String userIdStr = subscription.getMetadata().get("user_id");

            if(userIdStr != null) {
                int userId = Integer.parseInt(userIdStr);
                UserDAO userDao = new UserDAO();
                UserService userService = new UserService(userDao);
                User user = userService.getUserById(userId);

                if(user != null) {
                    System.out.println("============================");
                    System.out.println("Found user: " + user.getUsername());
                    System.out.println("============================");

                    user.setRole("trainer");

                    boolean updated = userService.updateUser(user);

                    if(updated) {
                        System.out.println("============================");
                        System.out.println("Updated user to trainer");
                        System.out.println("============================");
                    } else {
                        System.out.println("============================");
                        System.err.println("Failed to update user");
                        System.out.println("============================");
                    }
                } else {
                    System.err.println("User not found");
                }
            } else {
                System.err.println("No user_id in metadata");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ✅ Checkout Session Creation
    public static void createCheckoutSession(Context ctx) {
        try {
            int userId = ctx.attribute("userId");
            Session session = stripeService.createSubscriptionSession(userId);

            System.out.println("\n\n💳 ===============================");
            System.out.println("🚀  Creating Stripe Checkout Session");
            System.out.println("👤  User ID: " + userId);
            System.out.println("💳  Session URL: " + session.getUrl());
            System.out.println("💳 ===============================");

            Map<String, String> response = new HashMap<>();
            response.put("id", session.getId());
            response.put("url", session.getUrl());

            ctx.json(response);
        } catch (StripeException e) {
            e.printStackTrace();
            ctx.status(500).json(Collections.singletonMap("error", e.getMessage()));
        }
    }
}
