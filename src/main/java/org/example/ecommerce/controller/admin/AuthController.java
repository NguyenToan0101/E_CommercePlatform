package org.example.ecommerce.controller.admin;

import org.example.ecommerce.model.User;
import org.example.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("api/v1/user")
public class AuthController {
    private final UserService userService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginlogin() {

        return "admin/login";
    }
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model, Locale locale) {
        if(userService.login(username, password)) {
            return "redirect:/api/v1/user/home";

        }else{
        model.addAttribute("error", "Invalid username or password");
        return "admin/login";
    }
    }
    @GetMapping("/home")
    public String homePage(Model model, Locale locale) {

        List<User> users = userService.findAll();
        model.addAttribute("users", users);
//        String greeting = "Heloo";
        String greeting = messageSource.getMessage("greeting", null, locale);
        model.addAttribute("greeting", greeting);
        return "admin/home";


}
}
