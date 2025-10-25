package com.jogobella.bookstore.service;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BasketServiceImpl implements BasketService {
    @Override
    public double calculatePrice(List<Integer> basket) {
        return 0;
    }
}
