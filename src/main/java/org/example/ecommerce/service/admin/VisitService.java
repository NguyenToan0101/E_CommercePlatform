package org.example.ecommerce.service.admin;

import org.example.ecommerce.entity.admin.PageVisitLog;
import org.example.ecommerce.repository.admin.PageVisitLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VisitService {

    @Autowired
    private PageVisitLogRepository logRepository;

    public void logVisit(String path, String device) {
        PageVisitLog log = new PageVisitLog();
        log.setPath(path);
        log.setDevice(device);
        log.setCreated_at(LocalDateTime.now());
        logRepository.save(log);
    }

    public long getVisitHome() {
        return logRepository.countHomeVisits();
    }

    public Integer countPageVisit(String path) {
        return logRepository.countPageVisitByPath(path);
    }

    public Integer countAllPageVisit() {
        return logRepository.countAllPageVisitLog();
    }
}

