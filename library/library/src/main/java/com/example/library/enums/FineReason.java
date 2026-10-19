package com.example.library.enums;

public enum FineReason {
    OVERDUE("Прострочення книги"),
    DAMAGE("Пошкодження книги");

    private final String label;
    FineReason(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
