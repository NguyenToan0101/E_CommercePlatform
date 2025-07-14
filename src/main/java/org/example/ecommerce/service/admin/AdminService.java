package org.example.ecommerce.service.admin;

import jakarta.persistence.EntityNotFoundException;
import org.example.ecommerce.common.dto.admin.permission.AdminDTO;
import org.example.ecommerce.common.dto.admin.permission.CreateAdminRequest;
import org.example.ecommerce.common.dto.AdminLoginResponse;
import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.admin.Admin;
import org.example.ecommerce.repository.admin.AdminRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Service
public class AdminService {
    private final AdminRepo adminRepo;

    public AdminService(AdminRepo adminRepo) {
        this.adminRepo = adminRepo;
    }

    public List<Admin> getAdmin() {
       return adminRepo.findAll();

    }
    public boolean isAdmin(String email, String password) {
        for (Admin admin : getAdmin()) {
            if(email.equalsIgnoreCase(admin.getEmail())&&admin.getPassword().equals(password)){
                return true;
            }

        }
        return false;
    }

    public List<AdminDTO> getAllMods() {
        return adminRepo.findAll().stream()
                .filter(a -> !a.getAdminid().equals(1))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private AdminDTO toDto(Admin a) {
        String role = a.getAdminid().equals(1) ? "Super Admin" : "Mod";
        return new AdminDTO(
                a.getAdminid(),
                a.getFullname(),
                a.getEmail(),
                a.getPhone(),
                role,
                a.getStatus(),
                a.getLastLogin(),
                a.getCreatedAt()
        );
    }


    //add
    public AdminDTO createMod(CreateAdminRequest req) {
        Admin a = new Admin();
        a.setFullname(req.getFullname());
        a.setEmail(req.getEmail());
        a.setPhone(req.getPhone());
        a.setPassword(req.getPassword());
        a.setStatus("active");
        LocalDateTime now = LocalDateTime.now();
        a.setCreatedAt(now);
        a.setLastLogin(now);
        Admin saved = adminRepo.save(a);
        return toDto(saved);
    }

    public void lockAdmin(Integer id) {
        Admin a = adminRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mod không tồn tại: " + id));
        a.setStatus("locked");
        a.setLastLogin(LocalDateTime.now());
        adminRepo.save(a);
    }

    public void unlockAdmin(Integer id) {
        Admin a = adminRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mod không tồn tại: " + id));
        a.setStatus("active");
        adminRepo.save(a);
    }

    //delete
    public void deleteMod(Integer id) {
        adminRepo.deleteById(id);
    }

    public AdminDTO findByEmail(String email) {
        Admin admin = adminRepo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Admin không tồn tại: " + email));
        return toDto(admin);
    }



}
