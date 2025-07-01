package org.example.ecommerce.service.admin;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.admin.Admin;
import org.example.ecommerce.repository.admin.AdminRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private final AdminRepo adminRepo;

    public AdminService(AdminRepo adminRepo) {
        this.adminRepo = adminRepo;
    }

    public List<Admin> getAdmin() {
       return adminRepo.findAll();

    }
//    public boolean isAdmin(String email, String password) {
//        for (Admin admin : getAdmin()) {
//            if(email.equalsIgnoreCase(admin.getEmail())&&admin.getPassword().equals(password)){
//                return true;
//            }
//
//        }
//        return false;
//    }
}
