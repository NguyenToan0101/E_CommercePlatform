package org.example.ecommerce.scheduler;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class AccountUnlockScheduler {

        @Autowired
        private CustomerRepository customerRepository;

        @Scheduled(fixedRate = 60000) // mỗi phút kiểm tra 1 lần
        public void unlockExpiredAccounts() {
            List<Customer> lockedUsers = customerRepository.findByIsLockedTrueAndLockedUntilBefore(LocalDateTime.now());

            for (Customer user : lockedUsers) {
                user.setLocked(false);
                user.setLockedUntil(null);
            }

            customerRepository.saveAll(lockedUsers);
        }
}

