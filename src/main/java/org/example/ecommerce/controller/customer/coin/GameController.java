package org.example.ecommerce.controller.customer.coin;

import jakarta.servlet.http.HttpSession;
import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.service.customer.coin.CoinService;
import org.example.ecommerce.service.customer.coin.GameService;
import org.example.ecommerce.service.customer.coin.PlayResultRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/game")
public class GameController {
    @Autowired
    private GameService gameService;
    @Autowired
    private HttpSession  session;

    @GetMapping("/game_now")
    public String listGame(Model model){
        Customer customer =(Customer)session.getAttribute("customer");
        model.addAttribute("customer",customer);
        return  "customer/coin/game";
    }

    @GetMapping("/xoc_dia")
    public String xocdia(Model model){
        Customer customer =(Customer)session.getAttribute("customer");
        if (customer == null){
            return "redirect:/login";
        }
        model.addAttribute("customer",customer);
        model.addAttribute("coin",gameService.getCoin(customer).getTotalXu());
        return  "customer/coin/xocdia";
    }

    @PostMapping("/result_xocdia")
    public ResponseEntity<?> playResult(@RequestBody PlayResultRequest request, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Chưa đăng nhập");
        }

        boolean isWin = request.getResult().equalsIgnoreCase(request.getBetChoice());
        gameService.updateCoinAfterGame(customer, isWin, request.getAmount());
        return ResponseEntity.ok(Map.of(
                "status", isWin ? "win" : "lose",
                "coin", gameService.getCoin(customer).getTotalXu()
        ));
    }

}
