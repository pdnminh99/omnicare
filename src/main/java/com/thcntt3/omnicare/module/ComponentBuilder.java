package com.thcntt3.omnicare.module;

import com.google.cloud.Timestamp;

import java.util.List;

public class ComponentBuilder {

    private String MAC;

    private int pinNumber = 0;

    private Timestamp lastRefresh;

    private List<RawData> data;

    private boolean isActive = false;

    public ComponentBuilder setMAC(String MAC) {
        this.MAC = MAC;
        return this;
    }

    public ComponentBuilder setPinNumber(Integer pinNumber) {
        this.pinNumber = pinNumber;
        return this;
    }

    public ComponentBuilder setLastRefresh(Timestamp lastRefresh) {
        this.lastRefresh = lastRefresh;
        return this;
    }

    public ComponentBuilder setData(List<RawData> data) {
        this.data = data;
        return this;
    }

    public ComponentBuilder setIsActive(boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public ComponentBuilder addData(RawData data) {
        if (this.data == null) {
            this.data = List.of(data);
        } else this.data.add(data);
        return this;
    }

    public Component build() {
        return new Component(
                MAC,
                pinNumber,
                data,
                isActive,
                lastRefresh
        );
    }
}
