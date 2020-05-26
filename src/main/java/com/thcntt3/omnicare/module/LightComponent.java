package com.thcntt3.omnicare.module;

import com.google.cloud.Timestamp;

public class LightComponent extends Component {
    private Boolean isLightOn;

    public LightComponent(String parentModule, Integer pinNumber, ComponentType type, Boolean isLightOn, Timestamp lastRefresh) {
        super(parentModule, pinNumber, type, lastRefresh);
        this.isLightOn = isLightOn;
    }

    public Boolean getLightOn() {
        return isLightOn;
    }

    public void setLightOn(Boolean lightOn) {
        isLightOn = lightOn;
    }
}
