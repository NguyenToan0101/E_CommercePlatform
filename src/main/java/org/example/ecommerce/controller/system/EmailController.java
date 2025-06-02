package org.example.ecommerce.controller.system;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


@Controller
@RequestMapping("/system/email")
public class EmailController {
    private final JavaMailSender mailSender;

    public EmailController(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    @GetMapping("/send")
    public String sendEmail(){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("toan20171020@gmail.com");
            message.setTo("toan20171020@gmail.com");
            message.setSubject("Demo Send Email");
            message.setText("Hello World");
            mailSender.send(message);
            return "admin/login";
        }catch (Exception e){
            e.getMessage();
        }

        return "admin/home";
    }
    @GetMapping("/sendAttach")
    public String sendEmailWithAttachment(){
        try {

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom("toan20171020@gmail.com");
            helper.setTo("toan20171020@gmail.com");
            helper.setSubject("Demo Send Email");
            helper.setText("Hello World");
            helper.addAttachment("SEA",new File("C:\\Users\\ADMIN\\OneDrive\\Hình ảnh\\Saved Pictures\\sea.jpg"));
            mailSender.send(mimeMessage);
            return "admin/login";
        }catch (Exception e){
            e.getMessage();
        }

        return "admin/home";
    }

    @GetMapping("/sendHtml")
    public String sendHtmlEmail(){
        try {

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom("toan20171020@gmail.com");
            helper.setTo("toan20171020@gmail.com");
            helper.setSubject("HTML Email");
            try(InputStream inputStream= Objects.requireNonNull(EmailController.class.getResourceAsStream("/templates/admin/login.html"))){
                helper.setText(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8),true);
            }catch (Exception e) {
                throw new RuntimeException(e);
            }

            helper.addInline("SEA",new File("C:\\Users\\ADMIN\\OneDrive\\Hình ảnh\\Saved Pictures\\sea.jpg"));
            mailSender.send(mimeMessage);
            return "admin/login";
        }catch (Exception e){
            e.getMessage();
        }

        return "admin/home";
    }
}
