package org.example.ecommerce.repository;

import org.example.ecommerce.common.dto.admin.complaintManagement.ComplaintListDTO;
import org.example.ecommerce.entity.conplaint.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Integer> {
//    @Query("""
//        SELECT new org.example.ecommerce.common.dto.admin.complaintManagement.ComplaintListDTO(
//            c.complaintId,
//            c.customer.id,
//            c.customer.firstname,
//            c.customer.lastname,
//            c.customer.email,
//            c.customer.phone,
//            c.reason.description,
//            cp.product_id,
//            c.orderId,
//            c.category.name,
//            c.status,
//            c.createdAt
//        )
//        FROM Complaint c
//        LEFT JOIN org.example.ecommerce.entity.conplaint.ComplaintProduct cp
//             ON cp.complaint = c
//        ORDER BY c.createdAt DESC
//    """)
//    List<ComplaintListDTO> findAllForList();


//    @Query("""
//  SELECT new org.example.ecommerce.common.dto.admin.complaintManagement.ComplaintListDTO(
//    c.complaintId,
//    c.customer.id,
//    c.customer.firstname,
//    c.customer.lastname,
//    c.customer.email,
//    c.customer.phone,
//    c.reason.description,
//    cp.product_id,
//    cf.reviewId,
//    c.category.name,
//    c.status,
//    c.createdAt
//  )
//  FROM Complaint c
//  JOIN c.customer
//  JOIN c.reason
//  JOIN c.category
//
//  LEFT JOIN org.example.ecommerce.entity.conplaint.ComplaintProduct cp
//    ON cp.complaint = c
//    AND c.category.categoryId = 1
//  LEFT JOIN org.example.ecommerce.entity.conplaint.ComplaintFeedback cf
//    ON cf.complaint = c
//    AND c.category.categoryId = 2
//  ORDER BY c.createdAt DESC
//""")
//    List<ComplaintListDTO> findAllForList();


    //tesst

//    @Query("""
//  SELECT DISTINCT new org.example.ecommerce.common.dto.admin.complaintManagement.ComplaintListDTO(
//    c.complaintId,
//    c.customer.id,
//    c.customer.firstname,
//    c.customer.lastname,
//    c.customer.email,
//    c.customer.phone,
//    c.reason.description,
//    CASE WHEN c.category.name = 'product' THEN cp.product_id ELSE null END,
//    CASE WHEN c.category.name = 'feedback' THEN cf.reviewId ELSE null END,
//    c.category.name,
//    c.status,
//    c.createdAt
//  )
//  FROM Complaint c
//  LEFT JOIN c.product cp   WITH c.category.name = 'product'
//  LEFT JOIN c.feedback cf  WITH c.category.name = 'feedback'
//  ORDER BY c.createdAt DESC
//""")
//    List<ComplaintListDTO> findAllForList();

    //test 2
    @Query("""
  SELECT new org.example.ecommerce.common.dto.admin.complaintManagement.ComplaintListDTO(
    c.complaintId,
    c.customer.id,
    c.customer.firstname,
    c.customer.lastname,
    c.customer.email,
    c.customer.phone,
    c.reason.description,
    cp.product_id,
    cf.reviewId,
    c.category.name,
    c.status,
    c.createdAt
  )
  FROM Complaint c
    LEFT JOIN c.product cp
    LEFT JOIN c.feedback cf
  ORDER BY c.createdAt DESC
""")
    List<ComplaintListDTO> findAllForList();
}
