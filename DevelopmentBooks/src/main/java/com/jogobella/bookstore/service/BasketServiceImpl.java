package com.jogobella.bookstore.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
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


        // balanced basked approach
        // highestOccurrence value is also amount of baskets
        int basketSize = 5;
        int loopRuns = 0;
        Map<Double, List<Set<Integer>>> priceBasketMap = new HashMap<>();
        for (int j = basketSize; j > 0 && j >= highestOccurrence; j--) {
            Map<Integer, Set<Integer>> indexedList = new HashMap<>();
            List<Integer> itemsNotFitting = new ArrayList<>();
            for (int i = 1; i <= highestOccurrence; i++) {
                Set<Integer> balancedBasket = new HashSet<>();
                for (Map.Entry<Integer, Long> entry : itemOccurrences.entrySet()) {
                    if (entry.getValue() >= i) {
                        if (balancedBasket.size() >= basketSize) {
                            itemsNotFitting.add(entry.getKey());
                        } else {
                            balancedBasket.add(entry.getKey());
                        }
                    }
                }
                indexedList.put(i, balancedBasket);

                // check if possible to add elements to other arrays
                if (!itemsNotFitting.isEmpty()) {
                    for (Set<Integer> balancedBasketInMap: indexedList.values()) {
                        if (balancedBasketInMap.size() < basketSize) {
                            List<Integer> itemsToBeAdded = itemsNotFitting
                                    .stream()
                                    .filter(item -> !balancedBasketInMap.contains(item))
                                    .collect(Collectors.toList());

                            int counter = 0;
                            while (balancedBasketInMap.size() < basketSize) {
                                balancedBasketInMap.add(itemsToBeAdded.get(counter));
                                itemsToBeAdded.remove(counter);
                                counter++;
                            }
                        }
                    }
                }
            }

            LOG.info(indexedList.toString());
            List<Set<Integer>> balancedBaskets = indexedList.values().stream().collect(Collectors.toList());
            double price = calculateFinalPrice(balancedBaskets);
            priceBasketMap.put(price, balancedBaskets);
            basketSize--;

            loopRuns++;
            LOG.info("iteration no: " + loopRuns);
            if (loopRuns == highestOccurrence) break; // stop looping after number of occurrences happened
        }
        LOG.info(priceBasketMap.toString());

        LOG.info(priceBasketMap.keySet().toString());

        double minPrice = priceBasketMap.keySet().stream().mapToDouble(Double::doubleValue).min().orElse(0);
        LOG.info("final min price: {}", minPrice);
        return minPrice;
    }

    private double calculateFinalPrice(List<Set<Integer>> sortedBaskets) {
        double finalPrice = 0;
        for (Set<Integer> sortedBasket: sortedBaskets) {
            LOG.info("base price: {}, basket size: {}, discount: {}", BASE_PRICE, sortedBasket.size(), DISCOUNT_LIST.get(sortedBasket.size()));
            finalPrice += BASE_PRICE * sortedBasket.size() * DISCOUNT_LIST.get(sortedBasket.size());
        }
        return finalPrice;
    }
}
