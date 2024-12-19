package com.trade.app.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class MailUtil {
    @Autowired
    private static JavaMailSender mailSender;

    public static void sendOtpEmail(String recipientEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Your Two-Factor Authentication OTP");
        message.setText("Hello,\n\nYour OTP for login is: " + otp + "\n\nPlease use this to complete your login process.\n\nThank you!");

        mailSender.send(message);
    }

}
