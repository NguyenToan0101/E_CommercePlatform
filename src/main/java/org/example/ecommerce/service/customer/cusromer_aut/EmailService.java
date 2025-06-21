package org.example.ecommerce.service.customer.cusromer_aut;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public void sendVerificationEmail(String recipientEmail, String verifyURL) throws MessagingException {
        Context context = new Context();
        context.setVariable("verifyURL", verifyURL);

        String htmlContent = templateEngine.process("/customer/customer_aut/verification_email", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        helper.setTo(recipientEmail);
        helper.setSubject("Xác thực đăng ký tài khoản");
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    public void sendResetPasswordEmail(String recipientEmail, String resetURL) throws MessagingException {
        Context context = new Context();
        context.setVariable("resetURL", resetURL);

        String htmlContent = templateEngine.process("/customer/customer_aut/reset_password_email", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        helper.setTo(recipientEmail);
        helper.setSubject("Yêu cầu đặt lại mật khẩu");
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}
