package com.thcntt3.omnicare.module;

import com.google.cloud.Timestamp;
import org.springframework.lang.Nullable;

public class FireComponent extends Component {
    @Nullable
    private FireState state;

    public FireComponent(String parentModule, Integer pinNumber, ComponentType type, @Nullable FireState state, Timestamp lastRefresh) {
        super(parentModule, pinNumber, type, lastRefresh);
        this.state = state;
    }

    public FireState getState() {
        return state;
    }

    public void setState(FireState state) {
        this.state = state;
    }
}
