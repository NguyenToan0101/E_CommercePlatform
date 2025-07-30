package org.example.ecommerce.service.customer.wallet;

import org.example.ecommerce.entity.*;
import org.example.ecommerce.repository.*;
import org.example.ecommerce.service.PromotionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.time.Instant;
import java.util.*;

@Service
public class PaymentService {
    private final WalletRepository walletRepository;
    private final OrdersRepository ordersRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final CartitemRepository cartItemsRepository;
    private final CartRepository cartsRepository;
    private final InventoryRepository inventoryRepository;
    private final WalletHistoryRepository walletHistoryRepository;
    private final ProductimageRepository productimageRepository;
    private final PromotionService promotionService;
    private final PromotionRepository promotionRepository;

    public PaymentService(WalletRepository walletRepository, OrdersRepository ordersRepository, OrderItemsRepository orderItemsRepository, CartitemRepository cartItemsRepository, CartRepository cartsRepository, InventoryRepository inventoryRepository, WalletHistoryRepository walletHistoryRepository, ProductimageRepository productimageRepository, PromotionService promotionService, PromotionRepository promotionRepository) {
        this.walletRepository = walletRepository;
        this.ordersRepository = ordersRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.cartItemsRepository = cartItemsRepository;
        this.cartsRepository = cartsRepository;
        this.inventoryRepository = inventoryRepository;
        this.walletHistoryRepository = walletHistoryRepository;
        this.productimageRepository = productimageRepository;
        this.promotionService = promotionService;
        this.promotionRepository = promotionRepository;
    }

    public String checkout(Customer customer, List<Integer> cartItemIds, String fullname, String phone, String address, Integer walletId) {
        Wallet wallet = walletRepository.findByCustomerid(customer);

        Cart cart = cartsRepository.findByCustomerid(customer);

        List<Cartitem> selectedCartItems = cart.getCartitems().stream()
                .filter(item -> cartItemIds.contains(item.getId()))
                .toList();

        Map<Integer, List<Cartitem>> itemsByShop = new HashMap<>();
        for (Cartitem ci : selectedCartItems) {
            Integer shopId = ci.getProductid().getShopid().getId();
            itemsByShop.computeIfAbsent(shopId, k -> new ArrayList<>()).add(ci);
        }

        // Tính tổng tiền của tất cả sản phẩm được chọn
        BigDecimal totalAmountAllOrders = BigDecimal.ZERO;
        for (List<Cartitem> items : itemsByShop.values()) {
            for (Cartitem ci : items) {
                BigDecimal price = ci.getInventoryid().getPrice();
                BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(ci.getQuantity()));
                totalAmountAllOrders = totalAmountAllOrders.add(itemTotal);
            }
        }

        // Nếu người dùng chọn ví để thanh toán
        if (walletId != null) {
            // Kiểm tra số dư có đủ không
            if (wallet.getBalance().compareTo(totalAmountAllOrders) < 0) {
                return "Số dư ví không đủ, vui lòng nạp thêm tiền";
            }

            // Trừ tiền trong ví khách
            wallet.setBalance(wallet.getBalance().subtract(totalAmountAllOrders));
            walletRepository.save(wallet);

            // Ghi lại lịch sử trừ tiền
            WalletHistory history = new WalletHistory();
            history.setWalletid(wallet);
            history.setAmount(totalAmountAllOrders);
            history.setStatus("Decrease");
            history.setCreatedAt(LocalDateTime.now());
            walletHistoryRepository.save(history);
        }

        // Tạo đơn hàng cho từng shop
        for (Map.Entry<Integer, List<Cartitem>> entry : itemsByShop.entrySet()) {
            List<Cartitem> items = entry.getValue();

            // Tính tổng tiền từng đơn hàng
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (Cartitem ci : items) {
                BigDecimal price = ci.getInventoryid().getPrice();
                BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(ci.getQuantity()));
                totalAmount = totalAmount.add(itemTotal);
            }

            // Tạo đơn hàng
            Order order = new Order();
            order.setCustomerid(customer);
            order.setOrderdate(LocalDateTime.now());
            order.setTotalamount(totalAmount);
            order.setFullname(fullname);
            order.setPhone(phone);
            order.setAddress(address);
            order.setStatus("Chờ xác nhận");

            // Nếu thanh toán qua ví, đã thanh toán
            if (walletId != null) {
                order.setPaymentStatus("Đã thanh toán");
            } else {
                order.setPaymentStatus("Chưa thanh toán");
            }

            ordersRepository.save(order);

            // Xử lý từng sản phẩm trong đơn hàng
            for (Cartitem ci : items) {
                // Tạo OrderItem
                Orderitem oi = new Orderitem();
                oi.setOrderid(order);
                oi.setProductid(ci.getProductid());
                oi.setInventoryid(ci.getInventoryid());
                oi.setQuantity(ci.getQuantity());
                oi.setUnitprice(ci.getInventoryid().getPrice());
                orderItemsRepository.save(oi);

                // Nếu thanh toán qua ví thì cộng tiền cho người bán
                if (walletId != null) {
                    Customer seller = ci.getProductid().getShopid().getSellerid().getCustomer();
                    Wallet sellerWallet = seller.getWallet();
                    if (sellerWallet == null) {
                        sellerWallet = new Wallet();
                        sellerWallet.setCustomerid(seller);
                        sellerWallet.setBalance(BigDecimal.ZERO);
                        walletRepository.save(sellerWallet);
                    }

                    BigDecimal sellerAmount = ci.getInventoryid().getPrice().multiply(BigDecimal.valueOf(ci.getQuantity()));
                    sellerWallet.setBalance(sellerWallet.getBalance().add(sellerAmount));
                    walletRepository.save(sellerWallet);

                    // Ghi lại lịch sử cộng tiền
                    WalletHistory sellerHistory = new WalletHistory();
                    sellerHistory.setWalletid(sellerWallet);
                    sellerHistory.setAmount(sellerAmount);
                    sellerHistory.setStatus("Increase");
                    sellerHistory.setCreatedAt(LocalDateTime.now());
                    walletHistoryRepository.save(sellerHistory);
                }

                // Cập nhật kho: giảm tồn kho, tăng số lượng đã bán
                Inventory inventory = ci.getInventoryid();
                inventory.setQuantity(inventory.getQuantity() - ci.getQuantity());
                inventory.setSolditems(inventory.getSolditems() + ci.getQuantity());
                inventoryRepository.save(inventory);

                // Xóa sản phẩm khỏi giỏ hàng
                cartItemsRepository.delete(ci);
            }
        }

        return "Thanh toán thành công";
    }



    public String checkoutRealtime(Integer freeship,Integer discount ,BigDecimal price ,Customer customer, Integer inventoryId, Integer quantity, String fullname, String phone, String address) {
        Wallet wallet = walletRepository.findByCustomerid(customer);
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setCustomerid(customer);
            wallet.setBalance(BigDecimal.ZERO);
            walletRepository.save(wallet);
            return "Vui lòng nạp tiền vào ví trước khi thanh toán";
        }

        Product product = inventoryRepository.findById(inventoryId).get().getProductid();
        Inventory inventory = inventoryRepository.findById(inventoryId).orElse(null);

        if (inventory == null || inventory.getQuantity() < quantity) {
            return "Sản phẩm không đủ hàng trong kho";
        }

//        BigDecimal price = inventoryRepository.findFirstByProductidOrderByPriceAsc(product).getPrice();
//        BigDecimal price = inventory.getPrice();
        BigDecimal totalAmount = price.multiply(BigDecimal.valueOf(quantity));

        if (wallet.getBalance().compareTo(totalAmount) < 0) {
            return "Số dư ví không đủ, vui lòng nạp thêm tiền";
        }

        Order order = new Order();
        order.setCustomerid(customer);
        order.setOrderdate(LocalDateTime.now());
        order.setTotalamount(totalAmount);
        order.setFullname(fullname);
        order.setPhone(phone);
        order.setAddress(address);
        order.setStatus("Chờ lấy hàng");
        order.setPaymentStatus("Đã thanh toán");
        ordersRepository.save(order);

        wallet.setBalance(wallet.getBalance().subtract(totalAmount));
        walletRepository.save(wallet);

        WalletHistory history = new WalletHistory();
        history.setWalletid(wallet);
        history.setAmount(totalAmount);
        history.setStatus("Decrease");
        history.setCreatedAt(LocalDateTime.now());
        walletHistoryRepository.save(history);

        if (freeship != null) {
            Orderitem oi1 = new Orderitem();
            oi1.setOrderid(order);
            oi1.setProductid(product);
            oi1.setInventoryid(inventory);
            oi1.setQuantity(quantity);
            oi1.setUnitprice(price);
            oi1.setPromotionid(freeship);
            orderItemsRepository.save(oi1);
            Optional<Promotion> promotion = getPromotionById(freeship);
            promotion.orElseThrow().setRevenue(promotion.orElseThrow().getRevenue().add(price));
            promotion.orElseThrow().setOrders(promotion.orElseThrow().getOrders()+1);
            promotion.orElseThrow().setUsageCount(promotion.orElseThrow().getUsageCount()+1);
            promotionRepository.save(promotion.orElseThrow());
        }

        if (discount != null) {
            Orderitem oi2 = new Orderitem();
            oi2.setOrderid(order);
            oi2.setProductid(product);
            oi2.setInventoryid(inventory);
            oi2.setQuantity(quantity);
            oi2.setUnitprice(price);
            oi2.setPromotionid(discount);
            orderItemsRepository.save(oi2);
            Optional<Promotion> promotion = getPromotionById(discount);
            promotion.orElseThrow().setRevenue(promotion.orElseThrow().getRevenue().add(price));
            promotion.orElseThrow().setOrders(promotion.orElseThrow().getOrders()+1);
            promotion.orElseThrow().setUsageCount(promotion.orElseThrow().getUsageCount()+1);
            promotionRepository.save(promotion.orElseThrow());
        }



        Customer seller = product.getShopid().getSellerid().getCustomer();
        Wallet sellerWallet = walletRepository.findByCustomerid(seller);
        if (sellerWallet == null) {
            sellerWallet = new Wallet();
            sellerWallet.setCustomerid(seller);
            sellerWallet.setBalance(BigDecimal.ZERO);
            walletRepository.save(sellerWallet);
        }
        BigDecimal sellerAmount = price.multiply(BigDecimal.valueOf(quantity));
        sellerWallet.setBalance(sellerWallet.getBalance().add(sellerAmount));
        walletRepository.save(sellerWallet);

        WalletHistory sellerHistory = new WalletHistory();
        sellerHistory.setWalletid(sellerWallet);
        sellerHistory.setAmount(sellerAmount);
        sellerHistory.setStatus("Increase");
        sellerHistory.setCreatedAt(LocalDateTime.now());
        walletHistoryRepository.save(sellerHistory);

        inventory.setQuantity(inventory.getQuantity() - quantity);
        if(inventory.getSolditems() == null){
            inventory.setSolditems(quantity );
        }else {
            inventory.setSolditems(inventory.getSolditems() + quantity);
        }

        inventoryRepository.save(inventory);

        return "Thanh toán thành công";
    }

    public List<CartPreviewDTO> getCheckoutPreview(Customer customer, List<Integer> cartItemIds) {
        Cart cart = cartsRepository.findByCustomerid(customer);
        List<Cartitem> selectedCartItems = cart.getCartitems().stream().filter(item -> cartItemIds.contains(item.getId())).toList();

        List<CartPreviewDTO> previewItems = new ArrayList<>();
        for (Cartitem ci : selectedCartItems) {
            CartPreviewDTO dto = new CartPreviewDTO();
            dto.setId(ci.getId());
            dto.setProductName(ci.getProductid().getName());
            String imageUrl = ci.getInventoryid().getImage();
            dto.setImageUrl(imageUrl);

            dto.setQuantity(ci.getQuantity());
            dto.setPrice(ci.getInventoryid().getPrice());

            Inventory inventory = ci.getInventoryid();
            dto.setColor(inventory.getColor());
            dto.setDimension(inventory.getDimension());

            previewItems.add(dto);
        }

        return previewItems;
    }

    public CartPreviewDTO getCheckoutPreviewRealtime(Customer customer, Integer inventoryId, Integer quantity) {
        Inventory inventory = inventoryRepository.findById(inventoryId).orElse(null);
        Product product = inventory.getProductid();
        if (product != null && inventory != null) {
            CartPreviewDTO dto = new CartPreviewDTO();
            dto.setId(null);
            dto.setProductName(product.getName());

            String imageUrl = inventory.getImage();
            dto.setImageUrl(imageUrl);

            dto.setQuantity(quantity);
            dto.setInventoryId(inventoryId);
            dto.setPrice(inventory.getPrice());

            dto.setColor(inventory.getColor());
            dto.setDimension(inventory.getDimension());
            return dto;
        }
        return null;
    }
    public Product getProductById(Integer productId) {
        return inventoryRepository.findById(productId).map(Inventory::getProductid).orElse(null);
    }
    public Inventory getInventoryById(Integer inventoryId) {
        return inventoryRepository.findById(inventoryId).orElse(null);
    }
    public String getProvinceShopAddressById(Product product) {
       String[] partAddress =  product.getShopid().getFulladdress().split("-");
       return partAddress[partAddress.length - 1];
    }

    public Optional<Promotion> getPromotionById(Integer promotionId) {
        return promotionService.getPromotionById(promotionId);
    }
}