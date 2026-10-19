package com.example.library.enums;

public enum BookStatus {

    IN_STOCK("Є в наявності"),
    OUT_OF_STOCK("Немає в наявності"),
    RESERVED("Заброньовано"),
    ISSUED("Видано"),
    DAMAGED("Пошкоджено");

    private final String label;

    BookStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}