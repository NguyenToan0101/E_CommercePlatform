package org.example.ecommerce.controller.customer.customer_aut;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.service.customer.cusromer_aut.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

public class LoginController {
    @Autowired
    private CustomerService customerService;

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        Customer customer = customerService.login(email, password);
        if (customer != null) {
            session.setAttribute("customer", customer);
            session.setAttribute("role", customer.getRole());
            return "redirect:/home";
        }else{
            model.addAttribute("errorMessage", "email hoặc mật khẩu không đúng");
            return "login";
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
