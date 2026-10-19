package com.example.library.enums;

public enum CopyStatus {
    IN_STOCK("Доступна"),
    RESERVED("Заброньована"),
    ISSUED("Видана"),
    DAMAGED("Пошкоджена");

    private final String label;

    CopyStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
