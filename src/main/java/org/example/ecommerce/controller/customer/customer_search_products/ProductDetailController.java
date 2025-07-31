package org.example.ecommerce.controller.customer.customer_search_products;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.conplaint.ComplaintCategory;
import org.example.ecommerce.entity.conplaint.ComplaintReason;
import org.example.ecommerce.service.customer.customer_search_products.ProductDetail;
import org.example.ecommerce.service.customer.customer_search_products.ProductService;
import org.example.ecommerce.service.customer.customer_search_products.ReportService;
import org.example.ecommerce.service.customer.wishlist.WishlistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class ProductDetailController {

    @Autowired
    private ProductService productServices;
    @Autowired
    private WishlistService wishlistService;
    @Autowired
    private ReportService reportService;

    @GetMapping("/detailproduct")
    public String productDetail(@RequestParam("id") Integer id, Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        ProductDetail detail = productServices.getProductDetail(id);
        model.addAttribute("customer", customer);
        model.addAttribute("detail", detail);
        boolean isWishlisted = wishlistService.isWishlisted(customer, id);
        model.addAttribute("isWishlisted", isWishlisted);
        
        ComplaintCategory complaintCategoryProduct = reportService.getComplaintCategoryProduct();
        model.addAttribute("complaintCategoryProduct", complaintCategoryProduct);
        List<ComplaintReason> complaintReasonProduct = reportService.getComplaintReasonsProduct();
        model.addAttribute("complaintReasonProduct", complaintReasonProduct);

        ComplaintCategory complaintCategoryFeedback = reportService.getComplaintCategoryFeedback();
        model.addAttribute("complaintCategoryFeedback", complaintCategoryFeedback);
        List<ComplaintReason> complaintReasonFeedback = reportService.getComplaintReasonsFeedback();
        model.addAttribute("complaintReason", complaintReasonFeedback);


        return "customer/customer_search_product/product-detail";
    }

    @PostMapping("/report_product")
    public ResponseEntity<String> ReportProduct(@RequestParam("id") Integer id,
                                                @RequestParam("category") Integer category,
                                                @RequestParam("reasonId") Integer reasonId,
                                                @RequestParam(value = "description", required = false) String description,
                                                Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn cần đăng nhập");
        boolean result = reportService.createReportProduct(id,category,reasonId,description,customer);
        return ResponseEntity.ok(result ? "Báo cáo thành công" : "Lỗi thử lại sau");
    }

    @PostMapping("/report_review")
    public ResponseEntity<String> ReportFeedback(@RequestParam("id") Integer id,
                                                @RequestParam("category") Integer category,
                                                @RequestParam("reasonId") Integer reasonId,
                                                Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn cần đăng nhập");
        boolean result = reportService.createReportFeedback(id,category,reasonId,customer);
        return ResponseEntity.ok(result ? "Báo cáo thành công" : "Lỗi thử lại sau");
    }
}
