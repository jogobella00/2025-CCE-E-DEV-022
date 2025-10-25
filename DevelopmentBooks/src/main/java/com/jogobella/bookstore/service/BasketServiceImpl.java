package com.jogobella.bookstore.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class BasketServiceImpl implements BasketService {

    private final static Logger LOG = LoggerFactory.getLogger(BasketServiceImpl.class);

    private static final Map<Integer, Double> DISCOUNT_LIST = Map.of(
            1, 1.0,
            2, 0.95,
            3, 0.90,
            4, 0.80,
            5, 0.75
    );
    private static final double BASE_PRICE = 50.0;

    @Override
    public double calculatePrice(List<Integer> basket) {
        Map<Integer, Long> itemOccurrences = basket.stream().collect(Collectors.groupingBy(c -> c, Collectors.counting()));

        LOG.info(itemOccurrences.toString());

        // get the highest occurrence
        int highestOccurrence = (int) itemOccurrences
                .values()
                .stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0);
        LOG.info("highest occurrence found: {}", highestOccurrence);

        List<List<Integer>> sortedBasketItems = new ArrayList<>();
        // loop over the map, starting from the highest occurrence and create lists of discountable baskets
        for (int i = 1; i <= highestOccurrence; i++) {
            AtomicInteger counter = new AtomicInteger(i);
            LOG.info("checking for occurrence of {}", counter);
            sortedBasketItems.add(
                    itemOccurrences
                            .entrySet()
                            .stream()
                            .filter(item -> item.getValue() >= counter.longValue())
                            .map(Map.Entry::getKey)
                            .collect(Collectors.toList())
            );
        }

        LOG.info(sortedBasketItems.toString());
        return calculateFinalPrice(sortedBasketItems);
    }

    private double calculateFinalPrice(List<List<Integer>> sortedBaskets) {
        double finalPrice = 0;
        for (List<Integer> sortedBasket: sortedBaskets) {
            LOG.info("base price: {}, basket size: {}, discount: {}", BASE_PRICE, sortedBasket.size(), DISCOUNT_LIST.get(sortedBasket.size()));
            finalPrice += BASE_PRICE * sortedBasket.size() * DISCOUNT_LIST.get(sortedBasket.size());
        }
        return finalPrice;
    }
}
