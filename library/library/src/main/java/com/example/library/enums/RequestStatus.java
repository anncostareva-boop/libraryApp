package com.example.library.enums;

public enum RequestStatus {
    PENDING("Очікує на розгляд"),
    REJECTED("Відхилено"),
    AWAITING_ISSUANCE("Очікує на видачу"),
    ISSUED("Видано");

    private final String label;

    RequestStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
