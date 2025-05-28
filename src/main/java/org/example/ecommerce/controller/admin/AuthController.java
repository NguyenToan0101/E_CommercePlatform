package org.example.ecommerce.controller.admin;

import org.example.ecommerce.model.User;
import org.example.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("api/v1/user")
public class AuthController {
    private final UserService userService;
    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginlogin() {

        return "admin/login";
    }
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        if(userService.login(username, password)) {
            List<User> users = userService.findAll();
            model.addAttribute("users", users);
            return "admin/home";
        }else{
            model.addAttribute("error", "Invalid username or password");
            return "admin/login";
        }
    }

}
