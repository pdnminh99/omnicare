package com.thcntt3.omnicare.module;

public enum FireState {
    OFF("OFF"), SAFE("SAFE"), SMOKE("SMOKE"), FIRE("FIRE");

    FireState(String state) {
    }

    public static FireState fromText(String data) {
        if (data == null || data.trim().length() == 0) {
            return null;
        }
        switch (data) {
            case "FIRE":
                return FIRE;
            case "OFF":
                return OFF;
            case "SMOKE":
                return SMOKE;
            case "SAFE":
                return SAFE;
            default:
                return null;
        }
    }
}
