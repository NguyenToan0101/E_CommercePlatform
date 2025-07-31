package org.example.ecommerce.controller.customer.customer_aut;

import org.example.ecommerce.entity.Category;
import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Seller;
import org.example.ecommerce.entity.Shop;
import org.example.ecommerce.repository.CustomerRepository;
import org.example.ecommerce.repository.seller.ShopRepo;
import org.example.ecommerce.service.customer.customer_product.CustomerProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.net.URI;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.example.ecommerce.repository.seller.SellerRepo;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class HomeController {
    @Autowired
    private CustomerProductService productService;
    @Value("${api.frontend.seller}")
    private String frontendSellerUrl;
    private final CustomerRepository customerRepository;
    private final SellerRepo sellerRepository;
    private final ShopRepo shopRepository;

    public HomeController(CustomerRepository customerRepository, SellerRepo sellerRepository, ShopRepo shopRepository) {
        this.customerRepository = customerRepository;
        this.sellerRepository = sellerRepository;
        this.shopRepository = shopRepository;
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session, @RequestParam(value = "categoryPage", defaultValue = "1") int categoryPage) {
        Customer customer = (Customer) session.getAttribute("customer");
        List<Category> categories = productService.getCategories();

        int categoriesPerPage = 28;
        int totalCategories = categories.size();
        int totalCategoryPages = (int) Math.ceil((double) totalCategories / categoriesPerPage);
        if (categoryPage < 1) categoryPage = 1;
        if (categoryPage > totalCategoryPages) categoryPage = totalCategoryPages;
        int startIdx = (categoryPage - 1) * categoriesPerPage;
        int endIdx = Math.min(startIdx + categoriesPerPage, totalCategories);
        List<Category> pagedCategories = categories.subList(startIdx, endIdx);

        model.addAttribute("categories", pagedCategories);
        model.addAttribute("categoryPage", categoryPage);
        model.addAttribute("totalCategoryPages", totalCategoryPages);
        if (customer == null) {
            model.addAttribute("products", productService.getProductViews());
            return "customer/customer_aut/home";
        }
        model.addAttribute("customer", customer);
        model.addAttribute("products", productService.getProductViews());
        return "customer/customer_aut/home";
    }
    @GetMapping("/sellerChannel")
    public String redirectSellerChannel(HttpSession session,Model model){
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }
        try{

            if (customer.getSeller() == null) {
                return "redirect:/initSellerData";
            }
            Shop shop = customer.getSeller().getShop();
            if (shop.getStatus() == null) {
                return "redirect:/registrationSeller";
            }

            switch (shop.getStatus()) {
                case "PENDING_APPROVAL":
                    System.out.println("PENDING APPROVAL");
                    model.addAttribute("error", "Tài khoản người bán đang yêu cầu phê duyệt. Xin vui lòng đợi! Hãy thường xuyên kiểm tra thông báo");
                    return "redirect:/home";
                case "ACTIVE":
                    return "seller/index";
                case "LOCK":
                    System.out.println("LOCK");
                    model.addAttribute("error", "Tài khoản người bán đang bị khóa. Hãy liên hệ hỗ trợ hoặc gửi đến email admin@gmail.com");
                    return "redirect:/home";
                default:
                    return "redirect:/registrationSeller";
            }


        }catch (Exception e){
            return "redirect:/home";
        }
    }

    @GetMapping("/initSellerData")
    public String initSellerData(HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }

        if (customer.getSeller() == null) {
            Optional<Seller> sellerOpt = sellerRepository.findById(customer.getId());
            Seller seller = sellerOpt.orElseGet(() -> {
                Seller s = new Seller();
                s.setId(customer.getId());
                s.setCustomer(customer);
                return s;
            });

            Optional<Shop> shopOpt = shopRepository.findById(customer.getId());
            Shop shop = shopOpt.orElseGet(() -> {
                Shop sh = new Shop();
                sh.setId(customer.getId());
                sh.setSellerid(seller);
                return sh;
            });

            seller.setShop(shop);
            customer.setSeller(seller);
            customerRepository.save(customer);
            session.setAttribute("customer", customer);
        }

        return "redirect:/sellerChannel";

    }

    @GetMapping("/registrationSeller")
    public ResponseEntity<Void> registrationSeller() {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(frontendSellerUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

}
