package com.thcntt3.omnicare.module;

import com.google.cloud.Timestamp;

public class RawData {

    private ComponentType type;

    private String data;

    private Timestamp createdAt;

    public RawData() {
    }

    public RawData(String data, ComponentType type, Timestamp createdAt) {
        this.data = data;
        this.type = type;
        this.createdAt = createdAt;
    }

    public ComponentType getType() {
        return type;
    }

    public void setType(ComponentType type) {
        this.type = type;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}