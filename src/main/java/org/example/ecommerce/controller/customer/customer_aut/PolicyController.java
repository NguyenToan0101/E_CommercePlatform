package org.example.ecommerce.controller.customer.customer_aut;

import org.example.ecommerce.common.dto.catalogAndContent.ContentDetailDTO;
import org.example.ecommerce.service.admin.ContentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/policies")
public class PolicyController {
    private final ContentService contentService;
    public PolicyController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping
    public String showPolicies(Model model) {
        List<ContentDetailDTO> list = contentService.getPoliciesAndGuides();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (ContentDetailDTO content : list) {
            String formattedDate = content.getUpdatedAt().format(formatter);
            content.setFormattedUpdatedAt(formattedDate);
        }

        model.addAttribute("policies", list);
        return "customer/customer_aut/policies";
    }
}
