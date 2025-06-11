package org.example.ecommerce.service.customer.wallet;

import org.example.ecommerce.entity.*;
import org.example.ecommerce.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class PaymentService {
    private final WalletRepository walletRepository;
    private final OrdersRepository ordersRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final CartitemRepository cartItemsRepository;
    private final CartRepository cartsRepository;
    private final InventoryRepository inventoryRepository;
    private final WalletHistoryRepository walletHistoryRepository;

    public PaymentService(WalletRepository walletRepository, OrdersRepository ordersRepository, OrderItemsRepository orderItemsRepository, CartitemRepository cartItemsRepository, CartRepository cartsRepository, InventoryRepository inventoryRepository, WalletHistoryRepository walletHistoryRepository) {
        this.walletRepository = walletRepository;
        this.ordersRepository = ordersRepository;
        this.orderItemsRepository = orderItemsRepository;
        this.cartItemsRepository = cartItemsRepository;
        this.cartsRepository = cartsRepository;
        this.inventoryRepository = inventoryRepository;
        this.walletHistoryRepository = walletHistoryRepository;
    }

    public String checkout(Customer customer, List<Integer> cartItemIds) {
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

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Cartitem ci : selectedCartItems) {
            BigDecimal price = ci.getProductid().getPrice();
            BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(ci.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }


        if (wallet.getBalance().compareTo(totalAmount) < 0) {
            return "Số dư ví không đủ, vui lòng nạp thêm tiền";
        }



        Order order = new Order();
        order.setCustomerid(customer);
        order.setOrderdate(Instant.now());
        order.setTotalamount(totalAmount);
        order.setStatus("PAID");
        ordersRepository.save(order);

        wallet.setBalance(wallet.getBalance().subtract(totalAmount));
        walletRepository.save(wallet);

        WalletHistory history = new WalletHistory();
        history.setWalletid(wallet);
        history.setAmount(totalAmount);
        history.setStatus("Decrease");
        history.setCreatedAt(Instant.now());
        walletHistoryRepository.save(history);

        for (Cartitem ci : selectedCartItems) {
            Orderitem oi = new Orderitem();
            oi.setOrderid(order);
            oi.setProductid(ci.getProductid());
            oi.setInventoryid(ci.getInventoryid());
            oi.setQuantity(ci.getQuantity());
            oi.setUnitprice(ci.getProductid().getPrice());
            orderItemsRepository.save(oi);


            Inventory inventory = ci.getInventoryid();
            int newQuantity = inventory.getQuantity() - ci.getQuantity();
            inventory.setQuantity(newQuantity);
            inventory.setSolditems(inventory.getSolditems() + ci.getQuantity());
            inventoryRepository.save(inventory);

            cartItemsRepository.delete(ci);
        }

        return "Thanh toán thành công";
    }
}