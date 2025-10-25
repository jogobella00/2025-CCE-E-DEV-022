package com.jogobella.bookstore.model;

import java.util.List;

public class Book {

    private int id;
    private String name;

    public Book(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public static final List<Book> BOOKS_REPOSITORY = List.of(
            new Book(1, "Clean Code (Robert Martin, 2008)"),
            new Book(2, "The Clean Coder (Robert Martin, 2011)"),
            new Book(3, "Clean Architecture (Robert Martin, 2017)"),
            new Book(4, "Test Driven Development by Example (Kent Beck, 2003)"),
            new Book(5, "Working Effectively With Legacy Code (Michael C. Feathers, 2004)")
    );
}
