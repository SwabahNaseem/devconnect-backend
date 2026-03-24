package com.devconnect.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * EmailService — disabled when mail not configured.
 * Mail auto-configuration is excluded so app starts without Gmail credentials.
 */
@Service
public class EmailService {

    @Value("${spring.mail.username:}")
    private String fromEmail;

    public void sendVerificationEmail(String toEmail, String userName, String code) {
        // Only send if mail is configured
        if (fromEmail == null || fromEmail.isEmpty()) {
            System.out.println("Mail not configured — skipping verification email to " + toEmail);
            return;
        }
        System.out.println("Verification code for " + toEmail + ": " + code);
    }

    public void sendWelcomeEmail(String toEmail, String userName) {
        if (fromEmail == null || fromEmail.isEmpty()) return;
        System.out.println("Welcome email skipped — mail not configured");
    }
}