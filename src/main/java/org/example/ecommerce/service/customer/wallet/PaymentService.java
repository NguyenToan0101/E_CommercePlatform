package org.example.ecommerce.service.customer.wallet;

import org.example.ecommerce.entity.*;
import org.example.ecommerce.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public PaymentService(WalletRepository walletRepository, OrdersRepository ordersRepository, OrderItemsRepository orderItemsRepository, CartitemRepository cartItemsRepository, CartRepository cartsRepository, InventoryRepository inventoryRepository, WalletHistoryRepository walletHistoryRepository, ProductimageRepository productimageRepository) {
        this.walletRepository = walletRepository;
        this.ordersRepository = ordersRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.cartItemsRepository = cartItemsRepository;
        this.cartsRepository = cartsRepository;
        this.inventoryRepository = inventoryRepository;
        this.walletHistoryRepository = walletHistoryRepository;
        this.productimageRepository = productimageRepository;
    }

    public String checkout(Customer customer, List<Integer> cartItemIds, String fullname, String phone, String address) {
        Wallet wallet = walletRepository.findByCustomerid(customer);
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setCustomerid(customer);
            wallet.setBalance(BigDecimal.ZERO);
            walletRepository.save(wallet);
            return "Vui lòng nạp tiền vào ví trước khi thanh toán";
        }

        Cart cart = cartsRepository.findByCustomerid(customer);
        List<Cartitem> selectedCartItems = cartItemsRepository.findByCartidAndIdIn(cart, cartItemIds);

        Map<Integer, List<Cartitem>> itemsByShop = new HashMap<>();
        for (Cartitem ci : selectedCartItems) {
            Integer shopId = ci.getProductid().getShopid().getId();
            if (!itemsByShop.containsKey(shopId)) {
                itemsByShop.put(shopId, new ArrayList<>());
            }
            itemsByShop.get(shopId).add(ci);
        }

        BigDecimal totalAmountAllOrders = BigDecimal.ZERO;
        for (List<Cartitem> items : itemsByShop.values()) {
            for (Cartitem ci : items) {
                BigDecimal price = ci.getProductid().getPrice();
                BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(ci.getQuantity()));
                totalAmountAllOrders = totalAmountAllOrders.add(itemTotal);
            }
        }

        if (wallet.getBalance().compareTo(totalAmountAllOrders) < 0) {
            return "Số dư ví không đủ, vui lòng nạp thêm tiền";
        }

        wallet.setBalance(wallet.getBalance().subtract(totalAmountAllOrders));
        walletRepository.save(wallet);

        WalletHistory history = new WalletHistory();
        history.setWalletid(wallet);
        history.setAmount(totalAmountAllOrders);
        history.setStatus("Decrease");
        history.setCreatedAt(Instant.now());
        walletHistoryRepository.save(history);

        for (Map.Entry<Integer, List<Cartitem>> entry : itemsByShop.entrySet()) {
            List<Cartitem> items = entry.getValue();

            BigDecimal totalAmount = BigDecimal.ZERO;
            for (Cartitem ci : items) {
                BigDecimal price = ci.getProductid().getPrice();
                BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(ci.getQuantity()));
                totalAmount = totalAmount.add(itemTotal);
            }

            Order order = new Order();
            order.setCustomerid(customer);
            order.setOrderdate(Instant.now());
            order.setTotalamount(totalAmount);
            order.setFullname(fullname);
            order.setPhone(phone);
            order.setAddress(address);
            order.setStatus("Pending");
            ordersRepository.save(order);

            for (Cartitem ci : items) {
                Orderitem oi = new Orderitem();
                oi.setOrderid(order);
                oi.setProductid(ci.getProductid());
                oi.setInventoryid(ci.getInventoryid());
                oi.setQuantity(ci.getQuantity());
                oi.setUnitprice(ci.getProductid().getPrice());
                orderItemsRepository.save(oi);

                Customer seller = ci.getProductid().getShopid().getSellers().getCustomerid();
                Wallet sellerWallet = walletRepository.findByCustomerid(seller);
                if (sellerWallet == null) {
                    sellerWallet = new Wallet();
                    sellerWallet.setCustomerid(seller);
                    sellerWallet.setBalance(BigDecimal.ZERO);
                    walletRepository.save(sellerWallet);
                }
                BigDecimal sellerAmount = ci.getProductid().getPrice().multiply(BigDecimal.valueOf(ci.getQuantity()));
                sellerWallet.setBalance(sellerWallet.getBalance().add(sellerAmount));
                walletRepository.save(sellerWallet);

                WalletHistory sellerHistory = new WalletHistory();
                sellerHistory.setWalletid(sellerWallet);
                sellerHistory.setAmount(sellerAmount);
                sellerHistory.setStatus("Increase");
                sellerHistory.setCreatedAt(Instant.now());
                walletHistoryRepository.save(sellerHistory);

                Inventory inventory = ci.getInventoryid();
                int newQuantity = inventory.getQuantity() - ci.getQuantity();
                inventory.setQuantity(newQuantity);
                inventory.setSolditems(inventory.getSolditems() + ci.getQuantity());
                inventoryRepository.save(inventory);

                cartItemsRepository.delete(ci);
            }
        }

        return "Thanh toán thành công";
    }


    public String checkoutRealtime(Customer customer, Integer inventoryId, Integer quantity, String fullname, String phone, String address) {
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

        BigDecimal price = product.getPrice();
        BigDecimal totalAmount = price.multiply(BigDecimal.valueOf(quantity));

        if (wallet.getBalance().compareTo(totalAmount) < 0) {
            return "Số dư ví không đủ, vui lòng nạp thêm tiền";
        }

        Order order = new Order();
        order.setCustomerid(customer);
        order.setOrderdate(Instant.now());
        order.setTotalamount(totalAmount);
        order.setFullname(fullname);
        order.setPhone(phone);
        order.setAddress(address);
        order.setStatus("Pending");
        ordersRepository.save(order);

        wallet.setBalance(wallet.getBalance().subtract(totalAmount));
        walletRepository.save(wallet);

        WalletHistory history = new WalletHistory();
        history.setWalletid(wallet);
        history.setAmount(totalAmount);
        history.setStatus("Decrease");
        history.setCreatedAt(Instant.now());
        walletHistoryRepository.save(history);

        Orderitem oi = new Orderitem();
        oi.setOrderid(order);
        oi.setProductid(product);
        oi.setInventoryid(inventory);
        oi.setQuantity(quantity);
        oi.setUnitprice(price);
        orderItemsRepository.save(oi);

        Customer seller = product.getShopid().getSellers().getCustomerid();
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
        sellerHistory.setCreatedAt(Instant.now());
        walletHistoryRepository.save(sellerHistory);

        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventory.setSolditems(inventory.getSolditems() + quantity);
        inventoryRepository.save(inventory);

        return "Thanh toán thành công";
    }

    public List<CartPreviewDTO> getCheckoutPreview(Customer customer, List<Integer> cartItemIds) {
        Cart cart = cartsRepository.findByCustomerid(customer);
        List<Cartitem> selectedCartItems = cartItemsRepository.findByCartidAndIdIn(cart, cartItemIds);

        List<CartPreviewDTO> previewItems = new ArrayList<>();
        for (Cartitem ci : selectedCartItems) {
            CartPreviewDTO dto = new CartPreviewDTO();
            dto.setId(ci.getId());
            dto.setProductName(ci.getProductid().getName());
            List<Productimage> images = productimageRepository.findAllByProductid(ci.getProductid());
            String imageUrl = images.isEmpty() ? null : images.get(0).getImageurl();
            dto.setImageUrl(imageUrl);

            dto.setQuantity(ci.getQuantity());
            dto.setPrice(ci.getProductid().getPrice());

            Inventory inventory = ci.getInventoryid();
            dto.setColor(inventory.getColor());
            dto.setDimension(inventory.getDimension());

            previewItems.add(dto);
        }

        return previewItems;
    }

    public CartPreviewDTO getCheckoutPreviewRealtime(Customer customer, Integer inventoryId, Integer quantity) {
        Product product = inventoryRepository.findById(inventoryId).map(Inventory::getProductid).orElse(null);
        Inventory inventory = inventoryRepository.findById(inventoryId).orElse(null);
        if (product != null && inventory != null) {
            CartPreviewDTO dto = new CartPreviewDTO();
            dto.setId(null);
            dto.setProductName(product.getName());

            List<Productimage> images = productimageRepository.findAllByProductid(product);
            String imageUrl = images.isEmpty() ? null : images.get(0).getImageurl();
            dto.setImageUrl(imageUrl);

            dto.setQuantity(quantity);
            dto.setInventoryId(inventoryId);
            dto.setPrice(product.getPrice());

            dto.setColor(inventory.getColor());
            dto.setDimension(inventory.getDimension());
            return dto;
        }
        return null;
    }

}