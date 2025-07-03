    package org.example.ecommerce.controller.admin;

    import org.example.ecommerce.common.dto.admin.userManagement.UserDTO;
    import org.example.ecommerce.common.dto.admin.userManagement.LockRequest;
    import org.example.ecommerce.entity.Customer;
    import org.example.ecommerce.repository.CustomerRepository;
    import org.example.ecommerce.service.admin.AdminCustomerService;
    import org.example.ecommerce.service.customer.cusromer_aut.EmailService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;


    import java.time.LocalDateTime;
    import java.time.format.DateTimeFormatter;
    import java.util.List;
    import java.util.Optional;
    import jakarta.mail.MessagingException;


    @RestController
    @RequestMapping("/admin") // ← để khớp với frontend gọi /admin/users
    public class AdminUserController {

        @Autowired
        private AdminCustomerService adminCustomerService;

        @Autowired
        private CustomerRepository customerRepository;

        @Autowired
        private EmailService emailService;

        @GetMapping("/users") // ← đúng path rồi
        public List<UserDTO> getAllUsers() {
            return adminCustomerService.getAllCustomers();
        }

        @DeleteMapping("/users/{id}")
        public ResponseEntity<?> deleteCustomer(@PathVariable Integer id) {
            if (!customerRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            customerRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }

        // Khóa user
        @PostMapping("/users/{id}/lock")
        public ResponseEntity<?> lockUser(
                @PathVariable Integer id,
                @RequestBody LockRequest request
        ) {
            Optional<Customer> customerOpt = customerRepository.findById(id);
            if (customerOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            if (request == null || request.getDurationMinutes() == null || request.getDurationMinutes() <= 0) {
                return ResponseEntity.badRequest().body("durationMinutes is required and must be > 0");
            }

            Customer customer = customerOpt.get();
            LocalDateTime until = LocalDateTime.now().plusMinutes(request.getDurationMinutes());
            customer.setLockedUntil(until);
            customer.setLocked(true);
            customer.setStatus(false);
            customerRepository.save(customer);
            // Gửi email thông báo khóa tài khoản
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm 'ngày' dd/MM/yyyy");
                String untilStr = until.format(formatter);
                emailService.sendAccountLockedEmail(customer.getEmail(), untilStr);
            } catch (MessagingException e) {
                // Có thể log lỗi gửi email nếu cần
            }
            return ResponseEntity.ok().build();
        }

        // Mở khóa user
        @PutMapping("/users/{id}/unlock")
        public ResponseEntity<?> unlockUser(@PathVariable Integer id) {
            Optional<Customer> customerOpt = customerRepository.findById(id);
            if (customerOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Customer customer = customerOpt.get();
            customer.setLockedUntil(null);
            customer.setLocked(false);
            customer.setStatus(true);
            customerRepository.save(customer);
            // Gửi email thông báo mở khóa tài khoản
            try {
                emailService.sendAccountUnlockedEmail(customer.getEmail());
            } catch (jakarta.mail.MessagingException e) {
                // Có thể log lỗi gửi email nếu cần
            }
            return ResponseEntity.ok().build();
        }

    }
