package org.example.ecommerce.service.customer.coin;

import org.example.ecommerce.entity.*;
import org.example.ecommerce.repository.CoinRepository;
import org.example.ecommerce.repository.CoinTransactionRepository;
import org.example.ecommerce.repository.DailyCheckinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class CoinService {
    @Autowired
    private DailyCheckinRepository dailyCheckinRepository;
    @Autowired
    private CoinTransactionRepository coinTransactionRepository;
    @Autowired
    private CoinRepository coinRepository;

    private final int REWARD = 100;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM");

    public Map<DayOfWeek, Map<String, Object>> getWeekStatus(Customer customer) {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);

        Map<DayOfWeek, Map<String, Object>> result = new LinkedHashMap<>();
        for (int i = 0; i < 7; i++) {
            LocalDate date = startOfWeek.plusDays(i);
            boolean received = dailyCheckinRepository.existsByCustomerAndCheckinDate(customer, date);
            boolean isToday = date.equals(today);
            boolean isPast = date.isBefore(today);

            result.put(date.getDayOfWeek(), Map.of(
                    "date", date.format(dateFormatter),
                    "received", received,
                    "isToday", isToday,
                    "isPast", isPast,
                    "coinAmount", REWARD
            ));
        }
        return result;
    }

    public Coin getOrCreateCoin(Customer customer) {
        Coin coin = coinRepository.findByCustomer(customer);
        if (coin == null) {
            coin = new Coin();
            coin.setCustomer(customer);
            coin.setTotalXu(0);
            coinRepository.save(coin);
        }
        return coin;
    }

    @Transactional
    public String checkin(Customer customer) {
        LocalDate today = LocalDate.now();

        if (dailyCheckinRepository.existsByCustomerAndCheckinDate(customer, today)) {
            return "Bạn đã điểm danh hôm nay rồi!";
        }

        // Lưu điểm danh
        DailyCheckin checkin = new DailyCheckin();
        checkin.setCustomer(customer);
        checkin.setCheckinDate(today);
        dailyCheckinRepository.save(checkin);

        // Cập nhật ví
        Coin coin = getOrCreateCoin(customer);

        coin.setTotalXu(coin.getTotalXu() + REWARD);
        coinRepository.save(coin);

        // Lưu lịch sử giao dịch
        CoinTransaction tx = new CoinTransaction();
        tx.setCoin(coin);
        tx.setAmount(REWARD);
        tx.setStatus("INCREASE");
        coinTransactionRepository.save(tx);

        return "Điểm danh thành công! Bạn nhận được " + REWARD + " xu.";
    }
}
