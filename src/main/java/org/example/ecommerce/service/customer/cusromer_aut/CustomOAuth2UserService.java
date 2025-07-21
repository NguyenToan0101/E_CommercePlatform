package org.example.ecommerce.service.customer.cusromer_aut;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.repository.CustomerRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private HttpSession session;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        Customer customer = customerRepository.findByEmail(email);
        if (customer == null) {
            customer = new Customer();
            customer.setEmail(email);
            customer.setFirstname(name);
            customer.setLastname("");
            customer.setPhone("");
            customer.setRole("Customer");
            customer.setStatus(true);
            customer.setCreatedat(Instant.now());
//            customer.setSeller(new Seller(customer.getId()));
            customerRepository.save(customer);
        } else {
            // Nếu đã tồn tại, kiểm tra trạng thái khóa
            if (!customer.getStatus() || customer.isLocked()) {
                throw new OAuth2AuthenticationException("Tài khoản của bạn đã bị khóa");
            }
        }
        session.setAttribute("customer", customer);
        return oAuth2User;
    }
}