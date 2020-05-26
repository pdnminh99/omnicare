package com.thcntt3.omnicare.module;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.Exclude;
import org.springframework.lang.Nullable;

public class Component {
    @Exclude
    private String componentId;

    @JsonProperty
    @Nullable
    private Integer pinNumber;

    @Nullable
    @JsonProperty
    private Timestamp lastRefresh;

    @Nullable
    @JsonProperty
    private ComponentType type;

    public Component(String componentId,
                     @Nullable Integer pinNumber,
                     @Nullable ComponentType type,
                     @Nullable Timestamp lastRefresh) {
        this.componentId = componentId;
        this.pinNumber = pinNumber;
        this.type = type;
        this.lastRefresh = lastRefresh;
    }

    public static ComponentBuilder newBuilder(ComponentType type) {
        var builder = new ComponentBuilder();
        builder.setType(type);
        return builder;
    }

    @Nullable
    public Integer getPinNumber() {
        return pinNumber;
    }

    public void setPinNumber(@Nullable Integer pinNumber) {
        this.pinNumber = pinNumber;
    }

    @Exclude
    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    @Nullable
    public Timestamp getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(@Nullable Timestamp lastRefresh) {
        this.lastRefresh = lastRefresh;
    }

    @Nullable
    public ComponentType getType() {
        return type;
    }

    public void setType(@Nullable ComponentType type) {
        this.type = type;
    }
}