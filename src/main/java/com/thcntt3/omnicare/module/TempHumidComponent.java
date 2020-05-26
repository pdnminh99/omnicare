package com.thcntt3.omnicare.module;

import com.google.cloud.Timestamp;

public class TempHumidComponent extends Component {
    private Float temperature;

    private Float humidity;

    public TempHumidComponent(String parentModule, Integer pinNumber, ComponentType type, Float temperature, Float humidity, Timestamp lastRefresh) {
        super(parentModule, pinNumber, type, lastRefresh);
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public Float getHumidity() {
        return humidity;
    }

    public void setHumidity(Float humidity) {
        this.humidity = humidity;
    }
}
