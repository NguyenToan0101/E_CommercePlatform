package org.example.ecommerce.controller;

import org.example.ecommerce.entity.Conversation;
import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Message;
import org.example.ecommerce.entity.Seller;
import org.example.ecommerce.service.ChatService;
import org.example.ecommerce.repository.CustomerRepository;
import org.example.ecommerce.repository.seller.SellerRepo;
import org.example.ecommerce.repository.ShopRepository;
import org.example.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.util.stream.Collectors;
import org.example.ecommerce.entity.Shop;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.util.Map;

@Controller
public class ChatController {
    @Autowired
    private ChatService chatService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private SellerRepo sellerRepo;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private Cloudinary cloudinary;

    // Trang chat cho customer (hỗ trợ truyền conversationId để hiển thị hội thoại cụ thể)
    @GetMapping("/customer/chat")
    public String customerChatPage(@RequestParam(value = "conversationId", required = false) Integer conversationId, HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) return "redirect:/login";
        List<Conversation> conversations = chatService.getCustomerConversations(customer.getId());
        model.addAttribute("conversations", conversations);
        if (conversationId != null) {
            Conversation conversation = conversations.stream().filter(c -> c.getId().equals(conversationId)).findFirst().orElse(null);
            if (conversation != null) {
                List<Message> messages = chatService.getConversationMessages(conversationId);
                // Tạo list view model
                ArrayList<Map<String, Object>> messageViews = new ArrayList<>();
                String lastDate = null;
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd").withZone(ZoneId.systemDefault());
                for (Message msg : messages) {
                    String curDate = fmt.format(msg.getSentat());
                    boolean showDivider = lastDate == null || !curDate.equals(lastDate);
                    Map<String, Object> entry = new HashMap<>();
                    entry.put("message", msg);
                    entry.put("showDivider", showDivider);
                    messageViews.add(entry);
                    lastDate = curDate;
                }
                model.addAttribute("conversation", conversation);
                model.addAttribute("messageViews", messageViews);
                model.addAttribute("customer", customer);
            }
        }
        return "customer/chat";
    }

    // Trang chat cho seller (hỗ trợ truyền conversationId để hiển thị hội thoại cụ thể)
    @GetMapping("/seller/chat")
    public String sellerChatPage(@RequestParam(value = "conversationId", required = false) Integer conversationId, HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null || customer.getSeller() == null) return "redirect:/login";
        List<Conversation> conversations = chatService.getSellerConversations(customer.getSeller().getId());
        model.addAttribute("conversations", conversations);
        if (conversationId != null) {
            Conversation conversation = conversations.stream().filter(c -> c.getId().equals(conversationId)).findFirst().orElse(null);
            if (conversation != null) {
                List<Message> messages = chatService.getConversationMessages(conversationId);
                // Tạo list view model
                ArrayList<Map<String, Object>> messageViews = new ArrayList<>();
                String lastDate = null;
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd").withZone(ZoneId.systemDefault());
                for (Message msg : messages) {
                    String curDate = fmt.format(msg.getSentat());
                    boolean showDivider = lastDate == null || !curDate.equals(lastDate);
                    Map<String, Object> entry = new HashMap<>();
                    entry.put("message", msg);
                    entry.put("showDivider", showDivider);
                    messageViews.add(entry);
                    lastDate = curDate;
                }
                model.addAttribute("conversation", conversation);
                model.addAttribute("messageViews", messageViews);
            }
        }
        return "seller/chat";
    }

    // Tạo/lấy hội thoại từ product detail (customer -> shop)
    @PostMapping("/customer/chat/start")
    public String startChatWithShop(@RequestParam Integer shopId, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) return "redirect:/login";
        Seller seller = sellerRepo.findById(shopId).orElse(null);
        if (seller == null) return "redirect:/product/" + shopId;
        Conversation conversation = chatService.getOrCreateConversation(customer.getId(), seller.getId());
        return "redirect:/customer/chat?conversationId=" + conversation.getId();
    }

    // Tạo/lấy hội thoại từ seller (seller -> customer)
    @PostMapping("/seller/chat/start")
    public String startChatWithCustomer(@RequestParam Integer customerId, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null || customer.getSeller() == null) return "redirect:/login";
        Conversation conversation = chatService.getOrCreateConversation(customerId, customer.getSeller().getId());
        return "redirect:/seller/chat/conversation/" + conversation.getId();
    }

    // Trang hội thoại cụ thể (customer)
    @GetMapping("/customer/chat/conversation/{id}")
    public String customerConversation(@PathVariable Integer id, HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) return "redirect:/login";
        Conversation conversation = chatService.getCustomerConversations(customer.getId()).stream()
                .filter(c -> c.getId().equals(id)).findFirst().orElse(null);
        if (conversation == null) return "redirect:/customer/chat";
        List<Message> messages = chatService.getConversationMessages(id);
        model.addAttribute("conversation", conversation);
        model.addAttribute("messages", messages);
        return "customer/chat_conversation";
    }

    // Trang hội thoại cụ thể (seller)
    @GetMapping("/seller/chat/conversation/{id}")
    public String sellerConversation(@PathVariable Integer id, HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null || customer.getSeller() == null) return "redirect:/login";
        Conversation conversation = chatService.getSellerConversations(customer.getSeller().getId()).stream()
                .filter(c -> c.getId().equals(id)).findFirst().orElse(null);
        if (conversation == null) return "redirect:/seller/chat";
        List<Message> messages = chatService.getConversationMessages(id);
        model.addAttribute("conversation", conversation);
        model.addAttribute("messages", messages);
        return "seller/chat_conversation";
    }

    // Gửi tin nhắn (customer)
    @PostMapping("/customer/chat/conversation/{id}/send")
    public String customerSendMessage(@PathVariable Integer id, @RequestParam String content, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) return "redirect:/login";
        Conversation conversation = chatService.getCustomerConversations(customer.getId()).stream()
                .filter(c -> c.getId().equals(id)).findFirst().orElse(null);
        if (conversation == null) return "redirect:/customer/chat";
        Integer sellerId = conversation.getSellerid().getId();
        chatService.sendMessage(id, customer.getId(), sellerId, content);
        return "redirect:/customer/chat/conversation/" + id;
    }

    // Gửi tin nhắn (seller)
    @PostMapping("/seller/chat/conversation/{id}/send")
    public String sellerSendMessage(@PathVariable Integer id, @RequestParam String content, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null || customer.getSeller() == null) return "redirect:/login";
        Conversation conversation = chatService.getSellerConversations(customer.getSeller().getId()).stream()
                .filter(c -> c.getId().equals(id)).findFirst().orElse(null);
        if (conversation == null) return "redirect:/seller/chat";
        Integer customerId = conversation.getCustomerid().getId();
        chatService.sendMessage(id, customer.getSeller().getId(), customerId, content);
        return "redirect:/seller/chat/conversation/" + id;
    }

    // Tìm kiếm hội thoại theo tên shop (customer)
    @GetMapping("/customer/chat/search")
    public String searchCustomerConversations(@RequestParam String shopName, HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) return "redirect:/login";
        List<Conversation> conversations = chatService.searchCustomerConversationsByShopName(customer.getId(), shopName);
        model.addAttribute("conversations", conversations);
        return "customer/chat";
    }

    // Tìm kiếm hội thoại theo email customer (seller)
    @GetMapping("/seller/chat/search")
    public String searchSellerConversations(@RequestParam String customerEmail, HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null || customer.getSeller() == null) return "redirect:/login";
        List<Conversation> conversations = chatService.searchSellerConversationsByCustomerEmail(customer.getSeller().getId(), customerEmail);
        model.addAttribute("conversations", conversations);
        return "seller/chat";
    }

    // API tìm shop theo tên cho customer (trả về JSON)
    @GetMapping("/customer/chat/find-shop")
    @ResponseBody
    public List<Shop> findShopByName(@RequestParam String shopName, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) return Collections.emptyList();
        // Lấy id các seller đã có conversation với customer
        List<Conversation> conversations = chatService.getCustomerConversations(customer.getId());
        Set<Integer> sellerIds = conversations.stream().map(c -> c.getSellerid().getId()).collect(Collectors.toSet());
        // Tìm shop theo tên, loại trừ các shop đã có conversation
        List<Shop> shops = shopRepository.findByShopnameContainingIgnoreCase(shopName);
        return shops.stream().filter(shop -> !sellerIds.contains(shop.getSellerid().getId())).collect(Collectors.toList());
    }

    // API tìm customer theo email cho seller (trả về JSON)
    @GetMapping("/seller/chat/find-customer")
    @ResponseBody
    public List<Customer> findCustomerByEmail(@RequestParam String email, HttpSession session) {
        Customer current = (Customer) session.getAttribute("customer");
        if (current == null || current.getSeller() == null) return Collections.emptyList();
        Integer sellerId = current.getSeller().getId();
        // Lấy id các customer đã có conversation với seller
        List<Conversation> conversations = chatService.getSellerConversations(sellerId);
        Set<Integer> customerIds = conversations.stream().map(c -> c.getCustomerid().getId()).collect(Collectors.toSet());
        // Tìm customer theo email, loại trừ các customer đã có conversation
        List<Customer> customers = userRepository.findByEmailContainingIgnoreCase(email);
        return customers.stream().filter(c -> !customerIds.contains(c.getId())).collect(Collectors.toList());
    }

    // Upload ảnh cho chat (seller)
    @PostMapping("/seller/chat/upload-image")
    @ResponseBody
    public Map<String, String> uploadSellerChatImage(@RequestParam("image") MultipartFile image, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null || customer.getSeller() == null) {
            return Map.of("error", "Unauthorized");
        }
        
        try {
            if (image.isEmpty()) {
                return Map.of("error", "No image selected");
            }
            
            // Kiểm tra loại file
            String contentType = image.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return Map.of("error", "Invalid file type. Only images are allowed.");
            }
            
            // Kiểm tra kích thước file (max 5MB)
            if (image.getSize() > 5 * 1024 * 1024) {
                return Map.of("error", "File too large. Maximum size is 5MB.");
            }
            
            // Upload lên Cloudinary
            try {
                Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    image.getBytes(),
                    ObjectUtils.asMap(
                        "folder", "chat",
                        "public_id", "chat_" + System.currentTimeMillis(),
                        "resource_type", "image"
                    )
                );
                
                String imageUrl = (String) uploadResult.get("secure_url");
                return Map.of("imageUrl", imageUrl);
            } catch (Exception e) {
                return Map.of("error", "Failed to upload to Cloudinary: " + e.getMessage());
            }
        } catch (Exception e) {
            return Map.of("error", "Upload failed: " + e.getMessage());
        }
    }

    // Upload ảnh cho chat (customer)
    @PostMapping("/customer/chat/upload-image")
    @ResponseBody
    public Map<String, String> uploadCustomerChatImage(@RequestParam("image") MultipartFile image, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return Map.of("error", "Unauthorized");
        }
        
        try {
            if (image.isEmpty()) {
                return Map.of("error", "No image selected");
            }
            
            // Kiểm tra loại file
            String contentType = image.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return Map.of("error", "Invalid file type. Only images are allowed.");
            }
            
            // Kiểm tra kích thước file (max 5MB)
            if (image.getSize() > 5 * 1024 * 1024) {
                return Map.of("error", "File too large. Maximum size is 5MB.");
            }
            
            // Upload lên Cloudinary
            try {
                Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    image.getBytes(),
                    ObjectUtils.asMap(
                        "folder", "chat",
                        "public_id", "chat_" + System.currentTimeMillis(),
                        "resource_type", "image"
                    )
                );
                
                String imageUrl = (String) uploadResult.get("secure_url");
                return Map.of("imageUrl", imageUrl);
            } catch (Exception e) {
                return Map.of("error", "Failed to upload to Cloudinary: " + e.getMessage());
            }
        } catch (Exception e) {
            return Map.of("error", "Upload failed: " + e.getMessage());
        }
    }

} 