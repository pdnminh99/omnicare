package com.thcntt3.omnicare.module;

public enum ComponentType {
    FIRE("FIRE"), TEMP_HUMID("TEMP_HUMID"), LIGHT("LIGHT");

    private final String name;

    ComponentType(String name) {
        this.name = name;
    }

    public static ComponentType fromText(String type) {
        if (type == null || type.trim().length() == 0) {
            return null;
        }
        switch (type) {
            case "FIRE":
                return FIRE;
            case "TEMP_HUMID":
                return TEMP_HUMID;
            case "LIGHT":
                return LIGHT;
            default:
                return null;
        }
    }
}