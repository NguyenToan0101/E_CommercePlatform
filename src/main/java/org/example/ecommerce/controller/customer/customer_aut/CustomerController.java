package org.example.ecommerce.controller.customer.customer_aut;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.service.customer.cusromer_aut.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Controller
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "customer/customer_aut/register";
    }

//
//
//    @PostMapping("/register")
//    public String register(@ModelAttribute("customer") Customer customer,
//                           @RequestParam("imageFile") MultipartFile imageFile,
//                           HttpServletRequest request,
//                           Model model) {
//        try {
//            if (!imageFile.isEmpty()) {
//                customer.setImage(imageFile.getBytes());
//            }
//            String siteURL = request.getRequestURL().toString().replace(request.getServletPath(), "");
//            customerService.register(customer, siteURL);
//            model.addAttribute("message", "Vui lòng kiểm tra email để xác thực tài khoản");
//            return "customer/customer_aut/register_success";
//        } catch (Exception e) {
//            model.addAttribute("errorMessage", e.getMessage());
//            return "customer/customer_aut/register";
//        }
//    }
    @GetMapping("/verify")
    public String verifyCustomer(@RequestParam("token") String token, Model model) {
        if (customerService.verify(token)) {
            model.addAttribute("message", "Xác thực thành công. Bạn có thể đăng nhập.");
            return "customer/customer_aut/register_login";
        } else {
            model.addAttribute("errorMessage", "Token xác thực không hợp lệ hoặc đã hết hạn.");
            return "customer/customer_aut/register";
        }
    }





//    @GetMapping("/profile")
//    public String viewProfile(Model model, HttpSession session) {
//        Customer customer = (Customer) session.getAttribute("customer");
//        if (customer == null) {
//            return "redirect:/login";
//        }
//
//        if (customer.getImage() != null) {
//            String base64Image = Base64.getEncoder().encodeToString(customer.getImage());
//            model.addAttribute("base64Image", base64Image);
//        }
//        model.addAttribute("customer", customer);
//        return "customer/customer_aut/profile";
//    }





    @GetMapping("/change-password")
    public String showChangePasswordForm(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }
        return "customer/customer_aut/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 HttpSession session,
                                 Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }

        boolean isValid = customerService.checkPassword(customer.getEmail(), oldPassword);
        if (!isValid) {
            model.addAttribute("errorMessage", "Mật khẩu cũ không đúng");
            return "customer/customer_aut/change-password";
        }

        customerService.updatePassword(customer.getId(), newPassword);
        model.addAttribute("successMessage", "Đổi mật khẩu thành công");
        return "customer/customer_aut/change-password";
    }





    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
        return "customer/customer_aut/forgot_password";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request,
                                        @RequestParam("email") String email,
                                        Model model) {
        String siteURL = request.getRequestURL().toString().replace(request.getServletPath(), "");
        try {
            customerService.sendPasswordResetEmail(email, siteURL);
            model.addAttribute("message", "Vui lòng kiểm tra email để nhận link đặt lại mật khẩu.");
            return "customer/customer_aut/password_email";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "customer/customer_aut/forgot_password";
    }

    @GetMapping("/reset_password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "customer/customer_aut/reset_password";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(@RequestParam("token") String token,
                                       @RequestParam("password") String password,
                                       Model model) {
        if (customerService.resetPassword(token, password)) {
            model.addAttribute("message", "Đặt lại mật khẩu thành công. Bạn có thể đăng nhập.");
            return "login";
        } else {
            model.addAttribute("errorMessage", "Token không hợp lệ hoặc đã hết hạn.");
            model.addAttribute("token", token);
            return "customer/customer_aut/reset_password";
        }
    }




//
//    @GetMapping("/editprofile")
//    public String showProfile(Model model, HttpSession session) {
//        Customer customer = (Customer) session.getAttribute("customer");
//        String email = customer.getEmail();
//        if (email == null) {
//            return "redirect:/login";
//        }
//        if (customer.getImage() != null) {
//            String base64Image = Base64.getEncoder().encodeToString(customer.getImage());
//            model.addAttribute("base64Image", base64Image);
//        }
//        model.addAttribute("customer", customerService.findByEmail(email));
//        return "customer/customer_aut/profile_edit";
//    }

//    @PostMapping("/update")
//    public String updateProfile(@ModelAttribute("customer") Customer formCustomer,
//                                @RequestParam("imageFile") MultipartFile imageFile,
//                                Model model,
//                                HttpSession session) {
//        Customer customer = (Customer) session.getAttribute("customer");
//        String email = customer.getEmail();
//
//        Customer customer1 = customerService.findByEmail(email);
//
//        customer1.setFirstname(formCustomer.getFirstname());
//        customer1.setLastname(formCustomer.getLastname());
//        try {
//            if (imageFile != null && !imageFile.isEmpty()) {
//                customer1.setImage(imageFile.getBytes());
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        customer1.setPhone(formCustomer.getPhone());
//        customer1.setGender(formCustomer.getGender());
//        customer1.setDateofbirth(formCustomer.getDateofbirth());
//        customer1.setAddress(formCustomer.getAddress());
//
//        customerService.updateProfile(customer1);
//        session.setAttribute("customer", customer1);
//            return "redirect:/customers/profile";
//    }

}
