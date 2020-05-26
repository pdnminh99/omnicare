package com.thcntt3.omnicare.module;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.Timestamp;
import org.springframework.lang.Nullable;

public class ComponentBuilder {

    private String parentModule;

    private Integer pinNumber;

    private Timestamp lastRefresh;

    private String data;

    private Float temperature;

    private Float humidity;

    private Boolean isLightOn;

    private FireState fireState;

    private ComponentType type;

    public ComponentBuilder setComponentId(String parentModule) {
        this.parentModule = parentModule;
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

    public ComponentBuilder setType(ComponentType type) {
        this.type = type;
        return this;
    }

    public ComponentBuilder setData(String data) {
        this.data = data;
        if (type != null) {
            switch (type) {
                case TEMP_HUMID:
                    String[] raw = data.split("_");
                    if (raw.length > 0) {
                        temperature = Float.valueOf(raw[0]);
                        if (raw.length > 1) {
                            humidity = Float.valueOf(raw[1]);
                        }
                    }
                    break;
                case LIGHT:
                    isLightOn = Boolean.valueOf(data);
                    break;
                case FIRE:
                    fireState = FireState.fromText(data);
                    break;
            }
        }
        return this;
    }

    public ComponentBuilder setTemperature(Float temperature) {
        this.temperature = temperature;
        return this;
    }

    public ComponentBuilder setHumidity(Float humidity) {
        this.humidity = humidity;
        return this;
    }

    public ComponentBuilder setLightState(Boolean state) {
        this.isLightOn = state;
        return this;
    }

    public ComponentBuilder setFireState(FireState fireState) {
        this.fireState = fireState;
        return this;
    }

    public Component build() {
        if (type == null) {
            return new Component(parentModule, pinNumber, null, lastRefresh);
        }
        switch (type) {
            case FIRE:
                return new FireComponent(parentModule, pinNumber, type, fireState, lastRefresh);
            case LIGHT:
                return new LightComponent(parentModule, pinNumber, type, isLightOn, lastRefresh);
            case TEMP_HUMID:
                return new TempHumidComponent(parentModule, pinNumber, type, temperature, humidity, lastRefresh);
            default:
                return null;
        }
    }
}
