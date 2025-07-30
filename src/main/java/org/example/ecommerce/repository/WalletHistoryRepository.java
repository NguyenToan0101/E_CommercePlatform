package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Wallet;
import org.example.ecommerce.entity.WalletHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WalletHistoryRepository extends JpaRepository<WalletHistory, Integer> {
    List<WalletHistory> findAllByWalletidOrderByCreatedAtDesc(Wallet wallet);
    Page<WalletHistory> findAllByWalletidOrderByCreatedAtDesc(Wallet wallet, Pageable pageable);
}
