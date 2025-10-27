package com.jogobella.bookstore.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BasketService {

    double calculatePrice(List<Integer> basket);
}
