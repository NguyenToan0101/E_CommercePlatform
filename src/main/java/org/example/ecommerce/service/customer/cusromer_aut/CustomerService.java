package org.example.ecommerce.service.customer.cusromer_aut;

import org.example.ecommerce.entity.Customer;

import java.io.IOException;

public interface CustomerService {
    void register(Customer user, String siteURL) throws IOException;
    Customer login(String email, String password);
    boolean checkPassword(String email, String rawPassword);
    void updatePassword(Integer userId, String newPassword);
    boolean verify(String token);
    void sendPasswordResetEmail(String email, String siteURL);
    boolean resetPassword(String token, String newPassword);
    Customer findByEmail(String email);
    void updateProfile(Customer user);
}
