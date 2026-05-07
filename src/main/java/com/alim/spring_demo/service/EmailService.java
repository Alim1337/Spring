package com.alim.spring_demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {

    @Value("${resend.api.key:}")
    private String apiKey;

    @Value("${resend.from:DeliverFlow <onboarding@resend.dev>}")
    private String fromAddress;

    @Async
    public void sendHtml(String to, String subject, String htmlBody) {
        if (to == null || to.isBlank()) {
            log.warn("No recipient email — skipping");
            return;
        }
        if (apiKey == null || apiKey.isBlank() || apiKey.equals("re_your_key_here")) {
            log.warn("Resend API key not configured — skipping email to: {}", to);
            return;
        }

        log.info("Sending email to: {} | subject: {}", to, subject);

        try {
            Resend resend = new Resend(apiKey);

            CreateEmailOptions params = CreateEmailOptions.builder()
                .from(fromAddress)
                .to(to)
                .subject(subject)
                .html(htmlBody)
                .build();

            CreateEmailResponse response = resend.emails().send(params);
            log.info("✅ Email sent successfully | id: {}", response.getId());

        } catch (ResendException e) {
            log.error("❌ Email failed to: {} | error: {}", to, e.getMessage());
        }
    }

    // ── EMAIL TEMPLATES ────────────────────────────────────────────────────

    public String newDeliveryTemplate(String customerName, String businessName,
                                      String itemDescription, String trackingCode,
                                      String trackingUrl) {
        return """
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px;">
              <div style="background: #2563eb; padding: 20px; border-radius: 12px 12px 0 0; text-align: center;">
                <h1 style="color: white; margin: 0; font-size: 24px;">📦 New Delivery!</h1>
              </div>
              <div style="background: #f8fafc; padding: 24px; border-radius: 0 0 12px 12px; border: 1px solid #e2e8f0;">
                <p style="color: #374151; font-size: 16px;">Hi <strong>%s</strong>,</p>
                <p style="color: #374151;">You have a new delivery from <strong>%s</strong>:</p>
                <div style="background: white; padding: 16px; border-radius: 8px; border: 1px solid #e2e8f0; margin: 16px 0;">
                  <p style="color: #374151; margin: 0;"><strong>Item:</strong> %s</p>
                  <p style="color: #374151; margin: 8px 0 0 0;"><strong>Tracking code:</strong>
                    <code style="background: #f1f5f9; padding: 2px 6px; border-radius: 4px;">%s</code>
                  </p>
                </div>
                <a href="%s" style="display: block; background: #2563eb; color: white; text-align: center; padding: 14px; border-radius: 8px; text-decoration: none; font-weight: bold; margin-top: 16px;">
                  Track Your Delivery →
                </a>
                <p style="color: #9ca3af; font-size: 12px; text-align: center; margin-top: 20px;">
                  DeliverFlow — Smart delivery management
                </p>
              </div>
            </div>
            """.formatted(customerName, businessName, itemDescription, trackingCode, trackingUrl);
    }

    public String newDeliveryExternalTemplate(String recipientName, String businessName,
                                              String itemDescription, String trackingCode,
                                              String trackingUrl) {
        return """
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px;">
              <div style="background: #2563eb; padding: 20px; border-radius: 12px 12px 0 0; text-align: center;">
                <h1 style="color: white; margin: 0; font-size: 24px;">📦 You have a delivery!</h1>
              </div>
              <div style="background: #f8fafc; padding: 24px; border-radius: 0 0 12px 12px; border: 1px solid #e2e8f0;">
                <p style="color: #374151; font-size: 16px;">Hi <strong>%s</strong>,</p>
                <p style="color: #374151;"><strong>%s</strong> is sending you a delivery:</p>
                <div style="background: white; padding: 16px; border-radius: 8px; border: 1px solid #e2e8f0; margin: 16px 0;">
                  <p style="color: #374151; margin: 0;"><strong>Item:</strong> %s</p>
                  <p style="color: #374151; margin: 12px 0 4px 0;"><strong>Your tracking code:</strong></p>
                  <div style="background: #eff6ff; padding: 12px; border-radius: 8px; text-align: center; border: 1px dashed #bfdbfe;">
                    <code style="font-size: 22px; font-weight: bold; letter-spacing: 3px; color: #2563eb;">%s</code>
                  </div>
                </div>
                <a href="%s" style="display: block; background: #2563eb; color: white; text-align: center; padding: 14px; border-radius: 8px; text-decoration: none; font-weight: bold; margin-top: 16px;">
                  Track My Delivery →
                </a>
                <p style="color: #6b7280; font-size: 13px; text-align: center; margin-top: 16px;">
                  No account needed — just click the button above.
                </p>
                <p style="color: #9ca3af; font-size: 11px; text-align: center; margin-top: 8px;">
                  DeliverFlow — Smart delivery management
                </p>
              </div>
            </div>
            """.formatted(recipientName, businessName, itemDescription, trackingCode, trackingUrl);
    }

    public String deliveryAcceptedTemplate(String customerName, String driverName,
                                           String trackingCode, String trackingUrl) {
        return """
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px;">
              <div style="background: #16a34a; padding: 20px; border-radius: 12px 12px 0 0; text-align: center;">
                <h1 style="color: white; margin: 0; font-size: 24px;">🚗 Driver On The Way!</h1>
              </div>
              <div style="background: #f8fafc; padding: 24px; border-radius: 0 0 12px 12px; border: 1px solid #e2e8f0;">
                <p style="color: #374151; font-size: 16px;">Hi <strong>%s</strong>,</p>
                <p style="color: #374151;"><strong>%s</strong> has accepted your delivery!</p>
                <a href="%s" style="display: block; background: #16a34a; color: white; text-align: center; padding: 14px; border-radius: 8px; text-decoration: none; font-weight: bold; margin-top: 16px;">
                  Track Live →
                </a>
              </div>
            </div>
            """.formatted(customerName, driverName, trackingUrl);
    }

    public String deliveredTemplate(String customerName, String itemDescription,
                                    String businessName, String trackingUrl) {
        return """
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px;">
              <div style="background: #7c3aed; padding: 20px; border-radius: 12px 12px 0 0; text-align: center;">
                <h1 style="color: white; margin: 0; font-size: 24px;">🎉 Delivered!</h1>
              </div>
              <div style="background: #f8fafc; padding: 24px; border-radius: 0 0 12px 12px; border: 1px solid #e2e8f0;">
                <p style="color: #374151; font-size: 16px;">Hi <strong>%s</strong>,</p>
                <p style="color: #374151;">Your delivery from <strong>%s</strong> has arrived!</p>
                <div style="background: white; padding: 16px; border-radius: 8px; border: 1px solid #e2e8f0; margin: 16px 0; text-align: center;">
                  <p style="color: #374151; margin: 0; font-size: 18px;"><strong>%s</strong></p>
                  <p style="color: #9ca3af; font-size: 14px; margin: 4px 0 0 0;">Successfully delivered ✅</p>
                </div>
                <a href="%s" style="display: block; background: #7c3aed; color: white; text-align: center; padding: 14px; border-radius: 8px; text-decoration: none; font-weight: bold; margin-top: 16px;">
                  Rate Your Delivery →
                </a>
              </div>
            </div>
            """.formatted(customerName, businessName, itemDescription, trackingUrl);
    }
}