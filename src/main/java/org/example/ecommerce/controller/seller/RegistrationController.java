package org.example.ecommerce.controller.seller;



import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.example.ecommerce.dto.ShopRegistrationDTO;
import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Seller;
import org.example.ecommerce.entity.Shop;
import org.example.ecommerce.repository.UserRepository;
import org.example.ecommerce.repository.seller.SellerRepo;
import org.example.ecommerce.repository.seller.ShopRepo;
import org.example.ecommerce.service.seller.RegistrationService;
import org.example.ecommerce.service.seller.SellerService;
import org.example.ecommerce.service.seller.ShopService;
import org.hibernate.StaleObjectStateException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@CrossOrigin(origins = "https://seller-registration.vercel.app", allowCredentials = "true")
@RestController
@RequestMapping("/seller/registration")
public class RegistrationController {
    protected RegistrationService registrationService;
    protected  SellerService sellerService;
    private final UserRepository customerRepository;
    protected ShopService shopService;
    protected  SellerRepo sellerRepo;
    protected ShopRepo shopRepo;
    private Logger logger = LoggerFactory.getLogger(RegistrationController.class);
    private EntityManager em;

    public RegistrationController(RegistrationService registrationService, SellerService sellerService, UserRepository customerRepository, ShopService shopService, SellerRepo sellerRepo, ShopRepo shopRepo) {
        this.registrationService = registrationService;
        this.sellerService = sellerService;

        this.customerRepository = customerRepository;
        this.shopService = shopService;
        this.sellerRepo = sellerRepo;
        this.shopRepo = shopRepo;
    }



//    @PostMapping("/submit")
//    @Transactional
//    public ResponseEntity<?> getRegistration(@RequestBody ShopRegistrationDTO registrationDTO, HttpSession session) {
//        Customer customer = (Customer) session.getAttribute("customer");
//        if (customer == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
//        }
//
//        try {
//            // Refresh customer từ database để tránh stale state
//            Customer managedCustomer = customerRepository.findById(customer.getId()).orElse(null);
//            if (managedCustomer == null) {
//                return ResponseEntity.badRequest().body("Customer not found");
//            }
//
//            // Tạo hoặc cập nhật Seller entity
//            Seller seller = managedCustomer.getSeller();
//
//
//            if (seller == null) {
//                // Kiểm tra xem seller đã tồn tại trong DB chưa
//                Optional<Seller> existingSeller = sellerRepo.findById(managedCustomer.getId());
//                if (existingSeller.isPresent()) {
//                    seller = existingSeller.get();
//                } else {
//                    seller = new Seller();
//                    seller.setId(managedCustomer.getId());
//
//                }
//                seller.setCustomer(managedCustomer);
//                managedCustomer.setSeller(seller);
//            }
//
//            // Cập nhật thông tin seller
//            seller.setIdNumber(registrationDTO.getIdNumber());
//
//            // Xử lý file upload
//            if (registrationDTO.getFrontIdImage() != null && !registrationDTO.getFrontIdImage().trim().isEmpty()) {
//                seller.setFrontIdImage(registrationDTO.getFrontIdImage());
//            }
//            if (registrationDTO.getBackIdImage() != null && !registrationDTO.getBackIdImage().trim().isEmpty()) {
//                seller.setBackIdImage(registrationDTO.getBackIdImage());
//            }
//
//            // Tạo hoặc cập nhật Shop entity
//            Shop shop = seller.getShop();
//
//
//            if (shop == null) {
//                // Kiểm tra xem shop đã tồn tại trong DB chưa
//                Optional<Shop> existingShop = shopRepo.findById(seller.getId());
//                if (existingShop.isPresent()) {
//                    shop = existingShop.get();
//                } else {
//                    shop = new Shop();
//                    shop.setId(seller.getId());
//
//                }
//                shop.setSellerid(seller);
//                seller.setShop(shop);
//            }
//
//            // Cập nhật thông tin shop
//            shop.setShopname(registrationDTO.getShopName());
//            shop.setManageName(registrationDTO.getOwnerName());
//            shop.setPhone(registrationDTO.getPhone());
//            shop.setFulladdress(registrationDTO.getAddress().getFullAddress());
//            shop.setExpress(registrationDTO.getExpress());
//            shop.setFast(registrationDTO.getFast());
//            shop.setEconomy(registrationDTO.getEconomy());
//            shop.setLockerDelivery(registrationDTO.getLockerDelivery());
//            shop.setBulkyItems(registrationDTO.getBulkyItems());
//            shop.setBusinessType(registrationDTO.getBusinessType());
//            shop.setBusinessAddress(registrationDTO.getBusinessAddress());
//            shop.setInvoiceEmail(registrationDTO.getInvoiceEmail());
//            shop.setTaxCode(registrationDTO.getTaxCode());
//            shop.setStatus(Shop.Status.PENDING_APPROVAL.toString());
//
//            // Lưu theo thứ tự: Customer -> Seller -> Shop
//            managedCustomer = customerRepository.save(managedCustomer);
//
//            // Flush để đảm bảo data được persist
////            em.flush();
//
//            // Update session với managed entity
//            session.setAttribute("customer", managedCustomer);
//
//            return ResponseEntity.ok("Registration successful");
//
//        } catch (OptimisticLockException | StaleObjectStateException e) {
//            logger.error("Concurrent modification detected", e);
//            return ResponseEntity.status(HttpStatus.CONFLICT)
//                    .body("Data was modified by another user. Please refresh and try again.");
//        } catch (Exception e) {
//            logger.error("Registration failed", e);
//            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
//        }
//    }




}
