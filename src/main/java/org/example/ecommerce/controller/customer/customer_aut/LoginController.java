package org.example.ecommerce.controller.customer.customer_aut;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.service.admin.AdminService;
import org.example.ecommerce.service.customer.cusromer_aut.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;

@Controller
@CrossOrigin(origins ="http://localhost:3000", allowCredentials = "true")
public class LoginController {
    @Autowired
    private CustomerService customerService;
    private final AdminService adminService;

    public LoginController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        if(adminService.isAdmin(email,password)){
            return "redirect:/adminHome";
        }
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
