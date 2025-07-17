package org.example.ecommerce.service.customer.cusromer_aut;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.springframework.scheduling.annotation.Async;


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

    @Async
    public void sendAccountLockedEmail(String recipientEmail, String untilTime) throws MessagingException {
        String subject = "Tài khoản của bạn đã bị khóa";
        String content = "Tài khoản của bạn đã bị khóa do vi phạm chính sách của nền tảng đến " + untilTime + ". Nếu có bất kì thắc mắc/khiếu nại nào vui lòng reply email này.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        helper.setTo(recipientEmail);
        helper.setSubject(subject);
        helper.setText(content, false);

        mailSender.send(message);
    }

    @Async
    public void sendAccountUnlockedEmail(String recipientEmail) throws MessagingException {
        String subject = "Tài khoản của bạn đã được mở khóa";
        String content = "Tài khoản của bạn đã được mở khóa thành công. Chúc bạn có trải nghiệm tốt khi sử dụng nền tảng!";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        helper.setTo(recipientEmail);
        helper.setSubject(subject);
        helper.setText(content, false);

        mailSender.send(message);
    }

    @Async
    public void sendShopLockedEmail(String recipientEmail, String untilTime) throws MessagingException {
        String subject = "Shop của bạn đã bị khóa";
        String content = "Shop của bạn đã bị khóa do vi phạm chính sách của nền tảng đến " + untilTime + ". Nếu có bất kì thắc mắc/khiếu nại nào vui lòng reply email này.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        helper.setTo(recipientEmail);
        helper.setSubject(subject);
        helper.setText(content, false);

        mailSender.send(message);
    }

    @Async
    public void sendShopUnlockedEmail(String recipientEmail) throws MessagingException {
        String subject = "Shop của bạn đã được mở khóa";
        String content = "Shop của bạn đã được mở khóa thành công. Chúc bạn có trải nghiệm tốt khi sử dụng nền tảng!";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        helper.setTo(recipientEmail);
        helper.setSubject(subject);
        helper.setText(content, false);

        mailSender.send(message);
    }

    @Async
    public void sendSellerApprovedEmail(String recipientEmail) throws MessagingException {
        String subject = "Đăng ký bán hàng của bạn đã được duyệt";
        String content = "Xin chúc mừng! Tài khoản người bán của bạn đã được duyệt. Bạn có thể đăng nhập và bắt đầu bán hàng ngay bây giờ.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        helper.setTo(recipientEmail);
        helper.setSubject(subject);
        helper.setText(content, false);

        mailSender.send(message);
    }

    @Async
    public void sendSellerRejectedEmail(String recipientEmail) throws MessagingException {
        String subject = "Đăng ký bán hàng của bạn đã bị từ chối";
        String content = "Rất tiếc! Đăng ký người bán của bạn không đạt yêu cầu. Vui lòng liên hệ bộ phận hỗ trợ để biết thêm chi tiết.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        helper.setTo(recipientEmail);
        helper.setSubject(subject);
        helper.setText(content, false);

        mailSender.send(message);
    }


    //quản lí sản phẩm
    @Async
    public void sendProductApprovedEmail(String recipientEmail, String productName) throws MessagingException {
        String subject = "Sản phẩm của bạn đã được duyệt";
        String content = "Sản phẩm \"" + productName + "\" của bạn đã được duyệt và sẽ hiển thị trên cửa hàng ngay lập tức.";
        sendSimpleTextEmail(recipientEmail, subject, content);
    }

    @Async
    public void sendProductRejectedEmail(String recipientEmail, String productName) throws MessagingException {
        String subject = "Sản phẩm của bạn đã bị từ chối";
        String content = "Rất tiếc! Sản phẩm \"" + productName + "\" của bạn đã bị từ chối. Vui lòng kiểm tra lại và gửi lại.";
        sendSimpleTextEmail(recipientEmail, subject, content);
    }

    @Async
    public void sendProductLockedEmail(String recipientEmail, String productName, String untilTime) throws MessagingException {
        String subject = "Sản phẩm của bạn đã bị khóa";
        String content = "Sản phẩm \"" + productName + "\" của bạn đã bị khóa đến " + untilTime + ".";
        sendSimpleTextEmail(recipientEmail, subject, content);
    }

    @Async
    public void sendProductUnlockedEmail(String recipientEmail, String productName) throws MessagingException {
        String subject = "Sản phẩm của bạn đã được mở khóa";
        String content = "Sản phẩm \"" + productName + "\" của bạn đã được mở khóa thành công.";
        sendSimpleTextEmail(recipientEmail, subject, content);
    }

    // helper dùng chung
    private void sendSimpleTextEmail(String to, String subject, String content) throws MessagingException {
        MimeMessage msg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true, "utf-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, false);
        mailSender.send(msg);
    }

    //xóa
    @Async
    public void sendProductDeletedEmail(String recipientEmail, String productName) throws MessagingException {
        String subject = "Sản phẩm “" + productName + "” đã bị xóa";
        String content = "Sản phẩm “" + productName + "” của bạn đã bị xóa khỏi hệ thống. "
                + "Nếu có bất kỳ thắc mắc nào, vui lòng liên hệ bộ phận hỗ trợ.";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        helper.setTo(recipientEmail);
        helper.setSubject(subject);
        helper.setText(content, false);
        mailSender.send(message);
    }






}
