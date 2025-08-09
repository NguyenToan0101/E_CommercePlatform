package org.example.ecommerce.repository;

import org.example.ecommerce.common.dto.admin.complaintManagement.ComplaintListDTO;
import org.example.ecommerce.entity.conplaint.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Integer> {

    //test 2
    @Query("SELECT new org.example.ecommerce.common.dto.admin.complaintManagement.ComplaintListDTO("+
            "c.complaintId,"+
            "c.customer.id,"+
            "c.customer.firstname,"+
            "c.customer.lastname,"+
            "c.customer.email,"+
            "c.customer.phone,"+
            "c.reason.description,"+
            "cp.product_id, "+
            " rv.reviewId, "+
            " pm.paymentId, "+
            " sh.orderId, "+
            " c.category.name, "+
            " c.status, "+
            " c.createdAt) "+
            "FROM Complaint c "+
            " LEFT JOIN c.product cp " +
            "LEFT JOIN c.review rv " +
            "LEFT JOIN c.payment pm "+
            "LEFT JOIN c.shipping  sh "+
            " ORDER BY c.createdAt DESC"
    )
    List<ComplaintListDTO> findAllForList();

    // Seller view: list complaints by order id
    List<Complaint> findByOrderId(Integer orderId);

    // All complaints for a shop where the related order is a return/refund request
    @Query(value = "SELECT c.* FROM complaint c WHERE EXISTS (\n" +
            "  SELECT 1 FROM orders o\n" +
            "  JOIN orderitems oi ON oi.orderid = o.orderid\n" +
            "  JOIN products p ON p.productid = oi.productid\n" +
            "  WHERE o.orderid = c.order_id\n" +
            "    AND p.shopid = :shopId\n" +
            "    AND o.status = 'Yêu cầu trả hàng/hoàn tiền'\n" +
            ") ORDER BY c.created_at DESC", nativeQuery = true)
    List<Complaint> findReturnComplaintsByShopId(@Param("shopId") Integer shopId);
}
