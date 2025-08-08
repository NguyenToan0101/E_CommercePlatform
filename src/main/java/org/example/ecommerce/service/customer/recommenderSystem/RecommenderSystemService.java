package org.example.ecommerce.service.customer.recommenderSystem;


import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Product;
import org.example.ecommerce.entity.RecommenderSystem;
import org.example.ecommerce.repository.RecommenderSystemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RecommenderSystemService {

    private RecommenderSystemRepo recommenderSystemRepo;

    public RecommenderSystemService(RecommenderSystemRepo recommenderSystemRepo) {
        this.recommenderSystemRepo = recommenderSystemRepo;
    }

    public RecommenderSystemService() {

    }


    public void add(Customer customerId, Product productId, Integer rating) {
        RecommenderSystem recommenderSystem = new RecommenderSystem(customerId,productId,rating, LocalDateTime.now());

        recommenderSystemRepo.save(recommenderSystem);
    }
}
