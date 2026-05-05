package com.alim.spring_demo.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendHtml(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            helper.setFrom("noreply@deliverflow.app");
            mailSender.send(message);
            log.info("Email sent to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }

    // Email templates
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
                  <p style="color: #374151; margin: 8px 0 0 0;"><strong>Tracking code:</strong> <code style="background: #f1f5f9; padding: 2px 6px; border-radius: 4px; font-family: monospace;">%s</code></p>
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

    public String deliveryAcceptedTemplate(String customerName, String driverName,
                                            String trackingCode, String trackingUrl) {
        return """
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px;">
              <div style="background: #16a34a; padding: 20px; border-radius: 12px 12px 0 0; text-align: center;">
                <h1 style="color: white; margin: 0; font-size: 24px;">🚗 Driver On The Way!</h1>
              </div>
              <div style="background: #f8fafc; padding: 24px; border-radius: 0 0 12px 12px; border: 1px solid #e2e8f0;">
                <p style="color: #374151; font-size: 16px;">Hi <strong>%s</strong>,</p>
                <p style="color: #374151;">Great news! <strong>%s</strong> has accepted your delivery and is heading to pick it up.</p>
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
              <p style="color: #374151; margin: 12px 0 0 0;"><strong>Your tracking code:</strong></p>
              <div style="background: #f1f5f9; padding: 12px; border-radius: 8px; text-align: center; margin-top: 8px;">
                <code style="font-size: 20px; font-weight: bold; letter-spacing: 2px; color: #2563eb;">%s</code>
              </div>
            </div>
            <a href="%s" style="display: block; background: #2563eb; color: white; text-align: center; padding: 14px; border-radius: 8px; text-decoration: none; font-weight: bold; margin-top: 16px;">
              Track Your Delivery →
            </a>
            <p style="color: #9ca3af; font-size: 12px; text-align: center; margin-top: 20px;">
              No account needed — just click the button above or use your tracking code at our website.
            </p>
          </div>
        </div>
        """.formatted(recipientName, businessName, itemDescription, trackingCode, trackingUrl);
}
}