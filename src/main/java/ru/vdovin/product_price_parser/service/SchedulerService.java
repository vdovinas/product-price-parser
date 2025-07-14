package ru.vdovin.product_price_parser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchedulerService {
    private final AggregationService aggregationService;

    @Scheduled(cron = "0 0 * * * *")
    public void processCategories() {
        aggregationService.processSubcategories();
    }
}
