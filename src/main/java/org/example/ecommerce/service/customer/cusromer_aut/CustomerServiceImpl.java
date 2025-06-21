package org.example.ecommerce.service.customer.cusromer_aut;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CustomerServiceImpl implements CustomerService {
    LocalDateTime localDateTime = LocalDateTime.now();

    ZoneId zoneId = ZoneId.systemDefault();

    Instant instant = localDateTime.atZone(zoneId).toInstant();

    @Autowired
    private final UserRepository customerRepository;

    public CustomerServiceImpl(UserRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;
    private Map<String, Customer> pendingCustomers = new ConcurrentHashMap<>();



    @Override
    public void register(Customer customer, String siteURL) throws IOException {
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        if (customerRepository.existsByPhone(customer.getPhone())) {
            throw new RuntimeException("Số điện thoại đã tồn tại");
        }

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setRole("Customer");
        customer.setStatus(true);
        customer.setCreatedat(instant);

        String token = UUID.randomUUID().toString();

        pendingCustomers.put(token, customer);

        String verifyURL = siteURL + "/customers/verify?token=" + token;

        try {
            emailService.sendVerificationEmail(customer.getEmail(), verifyURL);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verify(String token) {
        Customer customer = pendingCustomers.get(token);
        if (customer == null) {
            return false;
        }
        customerRepository.save(customer);
        pendingCustomers.remove(token);
        return true;
    }

    @Override
    public Customer login(String email, String password) {
        
        Customer customer = customerRepository.findByEmail(email);
        if (customer != null && BCrypt.checkpw(password, customer.getPassword()) && customer.getStatus() == true) {
            return customer;
        }
        return null;
    }


    public boolean checkPassword(String email, String rawPassword) {
        Customer customer = customerRepository.findByEmail(email);
        if (customer == null) return false;
        return passwordEncoder.matches(rawPassword, customer.getPassword());
    }
    public void updatePassword(Integer customerId, String newPassword) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer != null) {
            String encodedPassword = passwordEncoder.encode(newPassword);
            customer.setPassword(encodedPassword);
            customerRepository.save(customer);
        }
    }


    private Map<String, Customer> passwordResetTokens = new ConcurrentHashMap<>();

    // gửi email lấy lại mật khẩu
    public void sendPasswordResetEmail(String email, String siteURL) {
        Customer customer = customerRepository.findByEmail(email);
        if (customer == null) {
            throw new RuntimeException("Email không tồn tại");
        }

        String token = UUID.randomUUID().toString();
        passwordResetTokens.put(token, customer);

        String resetURL = siteURL + "/customers/reset_password?token=" + token;
        try {
            emailService.sendResetPasswordEmail(email, resetURL);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    // reset mật khẩu
    public boolean resetPassword(String token, String newPassword) {
        Customer customer = passwordResetTokens.get(token);
        if (customer == null) {
            return false;
        }

        customer.setPassword(passwordEncoder.encode(newPassword));
        customerRepository.save(customer);
        passwordResetTokens.remove(token);
        return true;
    }

    @Override
    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public void updateProfile(Customer updatedCustomer) {
        // lấy customer_aut từ DB
        Customer customer = customerRepository.findById(updatedCustomer.getId()).orElse(null);
        if (customer != null) {
            customer.setFirstname(updatedCustomer.getFirstname());
            customer.setLastname(updatedCustomer.getLastname());
            if (updatedCustomer.getImage() != null && updatedCustomer.getImage().length > 0) {
                customer.setImage(updatedCustomer.getImage());
            }
            customer.setPhone(updatedCustomer.getPhone());
            customer.setAddress(updatedCustomer.getAddress());
            customer.setDateofbirth(updatedCustomer.getDateofbirth());
            customer.setGender(updatedCustomer.getGender());

            customerRepository.save(customer);
        }
    }
}
