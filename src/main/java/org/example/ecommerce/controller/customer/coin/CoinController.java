package org.example.ecommerce.controller.customer.coin;

import jakarta.servlet.http.HttpSession;
import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.service.customer.coin.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.DayOfWeek;
import java.util.Map;

@Controller
@RequestMapping("/checkin/coin")
public class CoinController {
    @Autowired
    private CoinService coinService;
    @Autowired
    private HttpSession session;

    @GetMapping
    public String showCheckinPage(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }
        
        Map<DayOfWeek, Map<String, Object>> weekStatus = coinService.getWeekStatus(customer);
        model.addAttribute("weekStatus", weekStatus);
        model.addAttribute("customer", customer);
        
        return "customer/coin/checkincoin";
    }

    @GetMapping("/week-status")
    public Map<DayOfWeek, Map<String, Object>> getWeekStatus(HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            throw new RuntimeException("Chưa đăng nhập");
        }
        return coinService.getWeekStatus(customer);
    }

    @PostMapping
    public ResponseEntity<String> checkin(HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Chưa đăng nhập");
        }
        return ResponseEntity.ok(coinService.checkin(customer));
    }
}


