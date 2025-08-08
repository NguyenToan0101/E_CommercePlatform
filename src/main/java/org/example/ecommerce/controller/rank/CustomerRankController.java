package org.example.ecommerce.controller.rank;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.CustomerRank;
import org.example.ecommerce.service.customer.rank.CustomerRankService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/rank")
public class CustomerRankController {

    @Autowired
    private CustomerRankService customerRankService;

    @GetMapping
    public String viewRankPage(Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }

        // Tự động tính lại rank mỗi khi truy cập
        customerRankService.calculateAndUpdateRank(customer.getId());
        
        // Lấy thông tin xếp hạng của khách hàng hiện tại
        CustomerRank customerRank = customerRankService.getCustomerRank(customer.getId());

        // Lấy danh sách tất cả xếp hạng để hiển thị bảng xếp hạng
        List<CustomerRank> allRanks = customerRankService.getAllRanks();

        model.addAttribute("customer", customer);
        model.addAttribute("customerRank", customerRank);
        model.addAttribute("allRanks", allRanks);

        return "customer/rank/rank";
    }

    @GetMapping("/all")
    public String viewAllRanks(Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }

        // Tự động tính lại rank mỗi khi truy cập
        customerRankService.calculateAndUpdateRank(customer.getId());
        
        List<CustomerRank> allRanks = customerRankService.getAllRanks();
        
        model.addAttribute("customer", customer);
        model.addAttribute("allRanks", allRanks);
        
        return "customer/rank/rank_all";
    }

    @GetMapping("/bronze")
    public String viewBronzeRanks(Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }

        // Tự động tính lại rank mỗi khi truy cập
        customerRankService.calculateAndUpdateRank(customer.getId());
        
        List<CustomerRank> bronzeRanks = customerRankService.getRanksByType("ĐỒNG");
        
        model.addAttribute("customer", customer);
        model.addAttribute("bronzeRanks", bronzeRanks);
        model.addAttribute("rankType", "ĐỒNG");
        
        return "customer/rank/rank_by_type";
    }

    @GetMapping("/silver")
    public String viewSilverRanks(Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }

        // Tự động tính lại rank mỗi khi truy cập
        customerRankService.calculateAndUpdateRank(customer.getId());
        
        List<CustomerRank> silverRanks = customerRankService.getRanksByType("BẠC");
        
        model.addAttribute("customer", customer);
        model.addAttribute("silverRanks", silverRanks);
        model.addAttribute("rankType", "BẠC");
        
        return "customer/rank/rank_by_type";
    }

    @GetMapping("/gold")
    public String viewGoldRanks(Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }

        // Tự động tính lại rank mỗi khi truy cập
        customerRankService.calculateAndUpdateRank(customer.getId());
        
        List<CustomerRank> goldRanks = customerRankService.getRanksByType("VÀNG");
        
        model.addAttribute("customer", customer);
        model.addAttribute("goldRanks", goldRanks);
        model.addAttribute("rankType", "VÀNG");
        
        return "customer/rank/rank_by_type";
    }

    @GetMapping("/platinum")
    public String viewPlatinumRanks(Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }

        // Tự động tính lại rank mỗi khi truy cập
        customerRankService.calculateAndUpdateRank(customer.getId());
        
        List<CustomerRank> platinumRanks = customerRankService.getRanksByType("BẠCH KIM");
        
        model.addAttribute("customer", customer);
        model.addAttribute("platinumRanks", platinumRanks);
        model.addAttribute("rankType", "BẠCH KIM");
        
        return "customer/rank/rank_by_type";
    }

    @GetMapping("/diamond")
    public String viewDiamondRanks(Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }

        // Tự động tính lại rank mỗi khi truy cập
        customerRankService.calculateAndUpdateRank(customer.getId());
        
        List<CustomerRank> diamondRanks = customerRankService.getRanksByType("KIM CƯƠNG");
        
        model.addAttribute("customer", customer);
        model.addAttribute("diamondRanks", diamondRanks);
        model.addAttribute("rankType", "KIM CƯƠNG");
        
        return "customer/rank/rank_by_type";
    }
}
