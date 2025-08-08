package org.example.ecommerce.controller.customer.model3D;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ModelController {

    @GetMapping("/view-model")
    public String viewModel() {
        return "customer/model3D/view-model";
    }

    @GetMapping("/virtual")
    public String virtualTryOn() {
        return "redirect:/quickstart-web/index.html";
    }


}
