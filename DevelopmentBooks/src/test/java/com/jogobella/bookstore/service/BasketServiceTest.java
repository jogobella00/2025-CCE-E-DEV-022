package com.jogobella.bookstore.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.jogobella.bookstore.model.Book.BOOKS_REPOSITORY;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Basket Service Test")
public class BasketServiceTest {

    BasketService basketService;

    @BeforeEach
    void setUp() {
        basketService = new BasketServiceImpl();
    }

    @Test
    @DisplayName("Should calculate price for one book")
    public void should_calculate_price_for_one_book() {
        assertEquals(50, basketService.calculatePrice(List.of(BOOKS_REPOSITORY.getFirst().id())));
    }

    @Test
    @DisplayName("Should calculate price for two different books")
    public void should_calculate_price_for_two_different_books() {
        List<Integer> basket = List.of(BOOKS_REPOSITORY.get(0).id(), BOOKS_REPOSITORY.get(1).id());
        // (50 + 50) - 5% = 95
        assertEquals(95, basketService.calculatePrice(basket));
    }

    @Test
    @DisplayName("Should calculate price for three different books")
    public void should_calculate_price_for_three_different_books() {
        List<Integer> basket = List.of(
                BOOKS_REPOSITORY.get(0).id(),
                BOOKS_REPOSITORY.get(1).id(),
                BOOKS_REPOSITORY.get(2).id());
        // (50 + 50 + 50) - 10% = 135
        assertEquals(135, basketService.calculatePrice(basket));
    }

    @Test
    @DisplayName("Should calculate price for four different books")
    public void should_calculate_price_for_four_different_books() {
        List<Integer> basket = List.of(
                BOOKS_REPOSITORY.get(0).id(),
                BOOKS_REPOSITORY.get(1).id(),
                BOOKS_REPOSITORY.get(2).id(),
                BOOKS_REPOSITORY.get(3).id());
        // (50 + 50 + 50 + 50) - 20% = 160
        assertEquals(160, basketService.calculatePrice(basket));
    }

    @Test
    @DisplayName("Should calculate price for five different books")
    public void should_calculate_price_for_five_different_books() {
        List<Integer> basket = List.of(
                BOOKS_REPOSITORY.get(0).id(),
                BOOKS_REPOSITORY.get(1).id(),
                BOOKS_REPOSITORY.get(2).id(),
                BOOKS_REPOSITORY.get(3).id(),
                BOOKS_REPOSITORY.get(4).id());
        // (50 + 50 + 50 + 50 + 50) - 25% = 187.5
        assertEquals(187.5, basketService.calculatePrice(basket));
    }

    @Test
    @DisplayName("Should calculate price for three different books + one extra")
    public void should_calculate_price_for_three_different_books_and_one_extra() {
        List<Integer> basket = List.of(
                BOOKS_REPOSITORY.get(0).id(),
                BOOKS_REPOSITORY.get(1).id(),
                BOOKS_REPOSITORY.get(2).id(),
                BOOKS_REPOSITORY.get(0).id());
        // ((50 + 50 + 50) - 20%) + 50 = 185
        assertEquals(185, basketService.calculatePrice(basket));
    }

    @Test
    @DisplayName("Should calculate price for a complex basket")
    public void should_calculate_price_for_complex_basket() {
        List<Integer> basket = List.of(
                // 2x Clean Code
                BOOKS_REPOSITORY.get(0).id(),
                BOOKS_REPOSITORY.get(0).id(),
                // 2x Clean Coder
                BOOKS_REPOSITORY.get(1).id(),
                BOOKS_REPOSITORY.get(1).id(),
                // 2x Clean Architecture
                BOOKS_REPOSITORY.get(2).id(),
                BOOKS_REPOSITORY.get(2).id(),
                // 1x TDD by Example
                BOOKS_REPOSITORY.get(3).id(),
                // 1x Working Effectively with Legacy code
                BOOKS_REPOSITORY.get(4).id());
        // ((50 + 50 + 50 + 50) - 20%) * 2 = 320
        assertEquals(320, basketService.calculatePrice(basket));
    }
}
