package com.thcntt3.omnicare.module;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.PropertyName;

import javax.validation.constraints.NotNull;
import java.util.List;

public class Component {

    @DocumentId
    private String componentId;

    private Integer pinNumber;

    private String MAC;

    private Timestamp lastRefresh;

    private List<RawData> data;

    private boolean isActive;

    public Component() {
    }

    public Component(
            @NotNull String MAC,
            @NotNull Integer pinNumber,
            List<RawData> data,
            boolean isActive,
            Timestamp lastRefresh) {
        this.MAC = MAC;
        this.pinNumber = pinNumber;
        setComponentId(pinNumber + "_" + MAC);
        this.data = data;
        this.isActive = isActive;
        this.lastRefresh = lastRefresh;
    }

    public static ComponentBuilder newBuilder() {
        return new ComponentBuilder();
    }

    public void setPinNumber(Integer pinNumber) {
        this.pinNumber = pinNumber;
    }

    public Integer getPinNumber() {
        return pinNumber;
    }

    @JsonGetter("MAC")
    @PropertyName("MAC")
    public String getMAC() {
        return MAC;
    }

    @PropertyName("MAC")
    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    @JsonIgnore
    public Timestamp getLastRefresh() {
        return lastRefresh;
    }

    @JsonGetter("lastRefresh")
    private Long getLastRefreshByEpoch() {
        return lastRefresh == null ? null : lastRefresh.toDate().getTime();
    }

    public void setLastRefresh(Timestamp lastRefresh) {
        this.lastRefresh = lastRefresh;
    }

    public Integer getDataCount() {
        return data == null ? null : data.size();
    }

    public List<RawData> getData() {
        return data;
    }

    public void setData(List<RawData> data) {
        this.data = data;
    }

    @JsonGetter("isActive")
    @PropertyName("isActive")
    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getComponentId() {
        return componentId;
    }
}