package com.devconnect.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

/**
 * EmailService — sends emails using Gmail SMTP.
 * Configured via application.properties spring.mail.* settings.
 */
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Send a 6-digit verification code to the user's email.
     * Called during registration.
     */
    public void sendVerificationEmail(String toEmail, String userName, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("DevConnect — Verify your email");

            // HTML email template
            String html =
                "<div style='font-family:Arial,sans-serif;max-width:480px;margin:0 auto;background:#0c1018;color:#f1f5f9;border-radius:12px;overflow:hidden'>" +
                "  <div style='background:linear-gradient(135deg,#2563eb,#3b82f6);padding:28px;text-align:center'>" +
                "    <h1 style='margin:0;font-size:24px;color:#fff'>DevConnect</h1>" +
                "    <p style='margin:8px 0 0;color:rgba(255,255,255,0.8);font-size:14px'>Team Finder Platform</p>" +
                "  </div>" +
                "  <div style='padding:32px'>" +
                "    <p style='font-size:16px;margin:0 0 8px'>Hi <strong>" + userName + "</strong>,</p>" +
                "    <p style='color:#94a3b8;font-size:14px;line-height:1.6;margin:0 0 28px'>Use the verification code below to complete your registration. This code expires in <strong>10 minutes</strong>.</p>" +
                "    <div style='background:#1a2334;border:2px solid #2563eb;border-radius:12px;padding:24px;text-align:center;margin:0 0 28px'>" +
                "      <p style='margin:0 0 8px;color:#64748b;font-size:12px;letter-spacing:2px;text-transform:uppercase'>Your verification code</p>" +
                "      <h2 style='margin:0;font-size:36px;letter-spacing:8px;color:#3b82f6;font-family:monospace'>" + code + "</h2>" +
                "    </div>" +
                "    <p style='color:#475569;font-size:12px;margin:0'>If you didn't create a DevConnect account, you can safely ignore this email.</p>" +
                "  </div>" +
                "</div>";

            helper.setText(html, true); // true = HTML
            mailSender.send(message);

        } catch (Exception e) {
            System.err.println("Failed to send verification email to " + toEmail + ": " + e.getMessage());
            throw new RuntimeException("Failed to send verification email. Please try again.");
        }
    }

    /**
     * Send a welcome email after successful verification.
     */
    public void sendWelcomeEmail(String toEmail, String userName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Welcome to DevConnect!");

            String html =
                "<div style='font-family:Arial,sans-serif;max-width:480px;margin:0 auto;background:#0c1018;color:#f1f5f9;border-radius:12px;overflow:hidden'>" +
                "  <div style='background:linear-gradient(135deg,#2563eb,#3b82f6);padding:28px;text-align:center'>" +
                "    <h1 style='margin:0;font-size:24px;color:#fff'>Welcome to DevConnect! 🚀</h1>" +
                "  </div>" +
                "  <div style='padding:32px'>" +
                "    <p style='font-size:16px;margin:0 0 16px'>Hi <strong>" + userName + "</strong>,</p>" +
                "    <p style='color:#94a3b8;font-size:14px;line-height:1.6;margin:0 0 16px'>Your email has been verified. You can now:</p>" +
                "    <ul style='color:#94a3b8;font-size:14px;line-height:2;margin:0 0 24px;padding-left:20px'>" +
                "      <li>Browse and join projects</li>" +
                "      <li>Create your own projects</li>" +
                "      <li>Chat with your team</li>" +
                "      <li>Get AI-powered match scores</li>" +
                "    </ul>" +
                "    <p style='color:#475569;font-size:12px;margin:0'>Happy building! — The DevConnect Team</p>" +
                "  </div>" +
                "</div>";

            helper.setText(html, true);
            mailSender.send(message);

        } catch (Exception e) {
            // Welcome email failure is non-critical, just log it
            System.err.println("Failed to send welcome email: " + e.getMessage());
        }
    }
}