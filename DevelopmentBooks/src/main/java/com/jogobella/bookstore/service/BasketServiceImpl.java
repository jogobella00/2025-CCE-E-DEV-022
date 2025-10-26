package com.jogobella.bookstore.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
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

        LOG.debug(itemOccurrences.toString());

        // get the highest occurrence
        int highestOccurrence = (int) itemOccurrences
                .values()
                .stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0);

        LOG.debug("highest occurrence found: {}", highestOccurrence);

        // balanced basked approach
        Map<Double, List<List<Integer>>> priceBasketMap = buildPriceBasketMap(itemOccurrences, highestOccurrence);
        LOG.debug("All basket combinations: {}", priceBasketMap);

        double minPrice = priceBasketMap.keySet()
                .stream()
                .mapToDouble(Double::doubleValue)
                .min()
                .orElse(0);

        LOG.debug("Final minimum price: {}", minPrice);
        return minPrice;
    }

    private double calculateFinalPrice(List<List<Integer>> sortedBaskets) {
        double finalPrice = 0;
        for (List<Integer> sortedBasket: sortedBaskets) {
            LOG.debug("base price: {}, basket size: {}, discount: {}", BASE_PRICE, sortedBasket.size(), DISCOUNT_LIST.get(sortedBasket.size()));
            finalPrice += BASE_PRICE * sortedBasket.size() * DISCOUNT_LIST.get(sortedBasket.size());
        }
        return finalPrice;
    }

    private Map<Double, List<List<Integer>>> buildPriceBasketMap(Map<Integer, Long> itemOccurrences, int highestOccurrence) {
        // highestOccurrence value is also amount of baskets
        int maxBasketSize = 5;
        Map<Double, List<List<Integer>>> priceBasketMap = new HashMap<>();

        // generate baskets with defined decreasing max size
        for (int j = highestOccurrence; j > 0; j--) {
            List<List<Integer>> balancedBaskets = generateBalancedBaskets(itemOccurrences, highestOccurrence, maxBasketSize);
            double price = calculateFinalPrice(balancedBaskets);
            priceBasketMap.put(price, balancedBaskets);
            maxBasketSize--;
        }

        return priceBasketMap;
    }

    private List<List<Integer>> generateBalancedBaskets(Map<Integer, Long> itemOccurrences, int highestOccurrence, int maxBasketSize) {
        Map<Integer, List<Integer>> indexedList = new HashMap<>();
        List<Integer> itemsNotFitting = new ArrayList<>();

        // Create baskets with max basket size defined + itemsNotFitting list
        // same logic as for biggest basket approach, but if basket reached max size, add element to a separate list, that will be addressed later
        for (int i = 1; i <= highestOccurrence; i++) {
            List<Integer> balancedBasket = new ArrayList<>();
            for (Map.Entry<Integer, Long> entry : itemOccurrences.entrySet()) {
                if (entry.getValue() >= i) {
                    if (balancedBasket.size() >= maxBasketSize || balancedBasket.contains(entry.getKey())) {
                        itemsNotFitting.add(entry.getKey());
                    } else {
                        balancedBasket.add(entry.getKey());
                    }
                }
            }
            indexedList.put(i, balancedBasket);
            fitItemsNotFitting(indexedList, itemsNotFitting, maxBasketSize);
        }

        return new ArrayList<>(indexedList.values());
    }

    private void fitItemsNotFitting(Map<Integer, List<Integer>> indexedList, List<Integer> itemsNotFitting, int maxBasketSize) {
        if (itemsNotFitting.isEmpty()) return;

        // Iterate over existing baskets and try to fit it elements from itemsNotFitting
        for (List<Integer> balancedBasket : indexedList.values()) {
            if (balancedBasket.size() < maxBasketSize) {
                List<Integer> itemsToAdd = itemsNotFitting.stream()
                        .filter(item -> !balancedBasket.contains(item))
                        .collect(Collectors.toList());

                Iterator<Integer> iterator = itemsToAdd.iterator();
                while (iterator.hasNext() && balancedBasket.size() < maxBasketSize) {
                    balancedBasket.add(iterator.next());
                    iterator.remove();
                }
            }
        }
    }
}
