package org.example.ecommerce.service.admin;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Product;
import org.example.ecommerce.entity.RecommenderSystem;
import org.example.ecommerce.entity.admin.PageVisitLog;
import org.example.ecommerce.repository.ProductRepository;
import org.example.ecommerce.repository.RecommenderSystemRepo;
import org.example.ecommerce.repository.admin.PageVisitLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VisitService {

    @Autowired
    private PageVisitLogRepository logRepository;
    @Autowired
    private RecommenderSystemRepo recommenderSystemRepo;
    @Autowired
    private ProductRepository productRepo;
    public void logVisit(String path, String device) {
        PageVisitLog log = new PageVisitLog();
        log.setPath(path);
        log.setDevice(device);
        log.setCreated_at(LocalDateTime.now());
        logRepository.save(log);
    }

    public long getVisitHome() {
        return logRepository.countHomeVisits();
    }

    public Integer countPageVisit(String path) {
        return logRepository.countPageVisitByPath(path);
    }

    public void add(Customer customerId, Product productId, Integer rating) {
        RecommenderSystem recommenderSystem = new RecommenderSystem(customerId,productId,rating,LocalDateTime.now());

        recommenderSystemRepo.save(recommenderSystem);
    }

    public Product getProduct(int productId) {
        return productRepo.findById(productId);
    }
    public Integer countAllPageVisit() {
        return logRepository.countAllPageVisitLog();
    }
}

