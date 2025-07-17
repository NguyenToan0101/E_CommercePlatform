package org.example.ecommerce.service.seller.wallet;

import org.example.ecommerce.common.dto.CashflowDTO;
import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Seller;
import org.example.ecommerce.entity.Wallet;
import org.example.ecommerce.entity.WalletHistory;
import org.example.ecommerce.repository.WalletHistoryRepository;
import org.example.ecommerce.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SellerWalletService {
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private WalletHistoryRepository walletHistoryRepository;

    public Wallet getOrCreateWalletForSeller(Seller seller) {
        Customer customer = seller.getCustomer();
        Wallet wallet = walletRepository.findByCustomerid(customer);
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setCustomerid(customer);
            wallet.setBalance(BigDecimal.ZERO);
            wallet = walletRepository.save(wallet);
        }
        return wallet;
    }

    public List<WalletHistory> getHistoryForSeller(Seller seller) {
        Wallet wallet = getOrCreateWalletForSeller(seller);
        return walletHistoryRepository.findAllByWalletidOrderByCreatedAtDesc(wallet);
    }

    public void deposit(Seller seller, BigDecimal amount) {
        Wallet wallet = getOrCreateWalletForSeller(seller);
        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
        WalletHistory history = new WalletHistory();
        history.setWalletid(wallet);
        history.setAmount(amount);
        history.setStatus("Increase");
        history.setCreatedAt(Instant.now());
        walletHistoryRepository.save(history);
    }
    // Lấy cashflow 30 ngày gần nhất cho seller
    public List<CashflowDTO> getCashflowForSeller(Seller seller) {
        LocalDate today = LocalDate.now();
        LocalDate from = today.minusDays(29);
        Wallet wallet = seller.getCustomer().getWallet();
        List<WalletHistory> history = walletHistoryRepository.findAllByWalletidOrderByCreatedAtDesc(wallet);
        Map<LocalDate, CashflowDTO> map = new HashMap<>();
        for (int i = 0; i < 30; i++) {
            LocalDate d = from.plusDays(i);
            map.put(d, new CashflowDTO(d, 0, 0));
        }
        for (WalletHistory h : history) {
            LocalDate d = h.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            if (d.isBefore(from) || d.isAfter(today)) continue;
            CashflowDTO dto = map.get(d);
            if (dto != null) {
                if ("Increase".equals(h.getStatus())) dto.setIncome(dto.getIncome() + h.getAmount().longValue());
                else dto.setExpense(dto.getExpense() + h.getAmount().longValue());
            }
        }
        return map.values().stream().sorted((a,b) -> a.getDate().compareTo(b.getDate())).collect(Collectors.toList());
    }

    public boolean withdraw(Seller seller, BigDecimal amount) {
        Wallet wallet = getOrCreateWalletForSeller(seller);
        if (wallet.getBalance().compareTo(amount) < 0) {
            return false;
        }
        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);
        WalletHistory history = new WalletHistory();
        history.setWalletid(wallet);
        history.setAmount(amount);
        history.setStatus("Decrease");
        history.setCreatedAt(java.time.Instant.now());
        walletHistoryRepository.save(history);
        return true;
    }

    public List<WalletHistory> getHistoryForSellerByDateRange(Seller seller, LocalDate from, LocalDate to) {
        Wallet wallet = getOrCreateWalletForSeller(seller);
        return walletHistoryRepository.findAllByWalletidOrderByCreatedAtDesc(wallet)
            .stream()
            .filter(h -> {
                LocalDate d = h.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDate();
                return (d.isEqual(from) || d.isAfter(from)) && (d.isEqual(to) || d.isBefore(to));
            })
            .toList();
    }

    public Page<WalletHistory> getHistoryForSellerByDateRangePaged(Seller seller, LocalDate from, LocalDate to, Pageable pageable) {
        Wallet wallet = getOrCreateWalletForSeller(seller);
        // Lấy tất cả theo date range, rồi phân trang thủ công (nếu cần), hoặc nếu muốn tối ưu thì cần custom query
        List<WalletHistory> filtered = walletHistoryRepository.findAllByWalletidOrderByCreatedAtDesc(wallet)
            .stream()
            .filter(h -> {
                LocalDate d = h.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDate();
                return (d.isEqual(from) || d.isAfter(from)) && (d.isEqual(to) || d.isBefore(to));
            })
            .toList();
        // Tạo Page thủ công
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filtered.size());
        List<WalletHistory> pageContent = filtered.subList(start, end);
        return new org.springframework.data.domain.PageImpl<>(pageContent, pageable, filtered.size());
    }
}
