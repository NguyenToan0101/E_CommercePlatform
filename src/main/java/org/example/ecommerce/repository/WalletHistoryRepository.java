package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Wallet;
import org.example.ecommerce.entity.WalletHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletHistoryRepository extends JpaRepository<WalletHistory, Integer> {
    List<WalletHistory> findAllByWalletidOrderByCreatedAtDesc(Wallet wallet);
}
