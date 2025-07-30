package org.example.ecommerce.service.customer.customer_search_products;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.conplaint.*;
import org.example.ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {
    @Autowired
    private ComplaintCategoryRepository complaintCategoryRepository;
    @Autowired
    private ComplaintReasonRepository complaintReasonRepository;
    @Autowired
    private ComplaintRepository complaintRepository;
    @Autowired
    private ComplaintProductRepository complaintProductRepository;
    @Autowired
    private ComplaintFeedbackRepository complaintFeedbackRepository;

    public ComplaintCategory getComplaintCategoryProduct(){
        return complaintCategoryRepository.findComplaintCategoriesByCategoryId((short) 1);
    }

    public List<ComplaintReason> getComplaintReasonsProduct(){
        return complaintReasonRepository.findAllByCategory(getComplaintCategoryProduct());
    }

    public ComplaintCategory getComplaintCategoryFeedback(){
        return complaintCategoryRepository.findComplaintCategoriesByCategoryId((short) 2);
    }
    public List<ComplaintReason> getComplaintReasonsFeedback(){
        return complaintReasonRepository.findAllByCategory(getComplaintCategoryFeedback());
    }


    public boolean createReportProduct(Integer id, Integer category, Integer reasonId, String description, Customer customer) {
        Complaint complaint = new Complaint();
        complaint.setCustomer(customer);
        complaint.setCategory(complaintCategoryRepository.findComplaintCategoriesByCategoryId((short) category.intValue()));
        complaint.setReason(complaintReasonRepository.findComplaintReasonByReasonId(reasonId));
        complaint.setDescription(description);
        complaint.setCreatedAt(LocalDateTime.now());
        complaintRepository.save(complaint);
        ComplaintProduct complaintProduct = new ComplaintProduct();
        complaintProduct.setComplaint(complaint);
        complaintProduct.setProduct_id(id);
        complaintProductRepository.save(complaintProduct);
        return true;
    }

    public boolean createReportFeedback(Integer id, Integer category, Integer reasonId, Customer customer) {
        Complaint complaint = new Complaint();
        complaint.setCustomer(customer);
        complaint.setCategory(complaintCategoryRepository.findComplaintCategoriesByCategoryId((short) category.intValue()));
        complaint.setReason(complaintReasonRepository.findComplaintReasonByReasonId(reasonId));
        complaint.setCreatedAt(LocalDateTime.now());
        complaintRepository.save(complaint);
        ComplaintFeedback complaintFeedback = new ComplaintFeedback();
        complaintFeedback.setComplaint(complaint);
        complaintFeedback.setReviewId(id);
        complaintFeedbackRepository.save(complaintFeedback);
        return true;
    }
}
