package org.example.ecommerce.controller.admin;

import org.example.ecommerce.model.User;
import org.example.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/home")
    public String homePage(Model model, Locale locale) {

        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        String greeting = messageSource.getMessage("greeting", null, locale);
        model.addAttribute("greeting", greeting);
        return "admin/home";


}
}
