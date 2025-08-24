package com.example.project.enums;

import lombok.Getter;

@Getter
public enum DonationType {
    ONLINE_UPI(1),
    CASH(2),
    CHEQUE(3),
    OTHER_ONLINE_SERVICE(4);

    private final int id;

    DonationType(int id) {
        this.id = id;
    }

    public static DonationType getDonationTypeFromId(int id) {
        for (DonationType donationType : DonationType.values()) {
            if (donationType.id == id) {
                return donationType;
            }
        }
        return null;
    }
}
