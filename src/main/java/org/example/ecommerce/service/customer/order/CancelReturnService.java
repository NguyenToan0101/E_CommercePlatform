package org.example.ecommerce.service.customer.order;

import jakarta.transaction.Transactional;
import org.example.ecommerce.entity.*;
import org.example.ecommerce.entity.conplaint.Complaint;
import org.example.ecommerce.entity.conplaint.ComplaintCategory;
import org.example.ecommerce.entity.conplaint.ComplaintImg;
import org.example.ecommerce.entity.conplaint.ComplaintReason;
import org.example.ecommerce.repository.*;
import org.example.ecommerce.service.UploadImageFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CancelReturnService {
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderItemsRepository orderItemsRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private WalletHistoryRepository walletHistoryRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private ComplaintReasonRepository complaintReasonRepository;
    @Autowired
    private ComplaintRepository complaintRepository;
    @Autowired
    private ComplaintImgRepository complaintImgRepository;
    @Autowired
    private ComplaintCategoryRepository complaintCategoryRepository;
    @Autowired
    private UploadImageFile uploadImageFile;

    @Transactional
    public boolean cancelOrderByCustomer(Integer orderId, Customer customer) {
        Order order = ordersRepository.findOrderById(orderId);
        BigDecimal totalAmount = order.getTotalamount();

        List<Orderitem> orderItems = orderItemsRepository.findAllByOrderid(order);
        for (Orderitem oi : orderItems) {
            Inventory inventory = oi.getInventoryid();
            int quantity = oi.getQuantity();
            inventory.setQuantity(inventory.getQuantity() + quantity);
            inventory.setSolditems(inventory.getSolditems() - quantity);
            inventoryRepository.save(inventory);
        }

        order.setStatus("Đã hủy");
        ordersRepository.save(order);

        Product product = order.getOrderitems().stream().findFirst().get().getProductid();
        Customer seller = product.getShopid().getSellerid().getCustomer();
        Wallet sellerWallet = walletRepository.findByCustomerid(seller);
        if (sellerWallet == null) {
            sellerWallet = new Wallet();
            sellerWallet.setCustomerid(seller);
            sellerWallet.setBalance(BigDecimal.ZERO);
        }
        BigDecimal newSellerBalance = sellerWallet.getBalance().subtract(totalAmount);
        sellerWallet.setBalance(newSellerBalance);
        walletRepository.save(sellerWallet);
        WalletHistory sellerHistory = new WalletHistory();
        sellerHistory.setWalletid(sellerWallet);
        sellerHistory.setAmount(totalAmount);
        sellerHistory.setStatus("Decrease");
        sellerHistory.setCreatedAt(Instant.now());
        walletHistoryRepository.save(sellerHistory);

        Wallet customerWallet = walletRepository.findByCustomerid(customer);
        if (customerWallet == null) {
            customerWallet = new Wallet();
            customerWallet.setCustomerid(customer);
            customerWallet.setBalance(BigDecimal.ZERO);
        }
        customerWallet.setBalance(customerWallet.getBalance().add(totalAmount));
        walletRepository.save(customerWallet);

        WalletHistory customerHistory = new WalletHistory();
        customerHistory.setWalletid(customerWallet);
        customerHistory.setAmount(totalAmount);
        customerHistory.setStatus("Increase");
        customerHistory.setCreatedAt(Instant.now());
        walletHistoryRepository.save(customerHistory);
        return true;
    }

    public List<ComplaintReason> getAllComplaintReasons(){
        return complaintReasonRepository.findAllByCategory(getAllComplaintCategories());
    }

    public ComplaintCategory getAllComplaintCategories(){
        return complaintCategoryRepository.findComplaintCategoriesByCategoryId((short) 3);
    }

    @Transactional
    public boolean createReturnRequest(Integer orderItemsId, Integer categoryId, Integer reasonId, String description, MultipartFile[] mediaFiles, Customer customer) {
        try {
            Orderitem orderItem = orderItemsRepository.findById(orderItemsId).orElse(null);
            if (orderItem == null) {
                System.out.println("[ERROR] Không tìm thấy orderItem với id=" + orderItemsId);
                return false;
            }
            Order oldOrder = orderItem.getOrderid();
            List<Orderitem> allItems = orderItemsRepository.findAllByOrderid(oldOrder);
            boolean onlyOne = allItems.size() == 1;
            Complaint complaint = new Complaint();

            if (!onlyOne) {
                Order newOrder = new Order();
                newOrder.setCustomerid(oldOrder.getCustomerid());
                newOrder.setOrderdate(Instant.now());
                newOrder.setTotalamount(orderItem.getUnitprice().multiply(BigDecimal.valueOf((orderItem.getQuantity()))));
                newOrder.setStatus("Yêu cầu trả hàng/hoàn tiền");
                newOrder.setPaymentStatus(oldOrder.getPaymentStatus());
                newOrder.setUpdatedAt(Instant.now());
                newOrder.setFullname(oldOrder.getFullname());
                newOrder.setPhone(oldOrder.getPhone());
                newOrder.setAddress(oldOrder.getAddress());
                ordersRepository.save(newOrder);

                orderItem.setOrderid(newOrder);
                orderItemsRepository.save(orderItem);

                BigDecimal remain = oldOrder.getTotalamount().subtract(newOrder.getTotalamount());
                oldOrder.setTotalamount(remain);
                ordersRepository.save(oldOrder);
                complaint.setOrderId(newOrder.getId());
            } else {
                oldOrder.setStatus("Yêu cầu trả hàng/hoàn tiền");
                ordersRepository.save(oldOrder);
                complaint.setOrderId(oldOrder.getId());
            }

            ComplaintCategory complaintCategory = complaintCategoryRepository.findById(categoryId).orElse(null);
            ComplaintReason complaintReason = complaintReasonRepository.findById(reasonId).orElse(null);

            complaint.setCustomer(customer);
            complaint.setCategory(complaintCategory);
            complaint.setReason(complaintReason);
            complaint.setDescription(description);
            complaint.setStatus("pending");
            complaint.setCreatedAt(LocalDateTime.now() );
            complaint.setUpdatedAt(LocalDateTime.now() );
            complaintRepository.save(complaint);

            if (mediaFiles != null) {
                for (MultipartFile file : mediaFiles) {
                    if (!file.isEmpty()) {
                        String url = uploadImageFile.uploadImage(file);
                        ComplaintImg img = new ComplaintImg();
                        img.setComplaint(complaint);
                        img.setImageUrl(url);
                        img.setCreatedAt(java.time.Instant.now());
                        complaintImgRepository.save(img);
                    }
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
