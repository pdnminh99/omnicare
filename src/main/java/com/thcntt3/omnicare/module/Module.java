package com.thcntt3.omnicare.module;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.annotation.Exclude;
import org.springframework.lang.Nullable;

public class Module {

    @Exclude
    private String MAC;

    @JsonProperty
    private String token;

    @JsonIgnore
    private Timestamp tokenRefreshedAt;

    @Nullable
    @JsonProperty
    private Timestamp lastRefresh;

    @Nullable
    @JsonProperty
    private String name;

    @Nullable
    @JsonProperty
    private Boolean isActive;

    @Exclude
    private Timestamp createTime;
    // Component:
    // - fire: FIRE; 0 - OFF, 1 - SAFE; 2 - SMOKE; 3 - FIRE
    // - temp-humid: TEMP_HUMID
    // - light: LIGHT

    public Module(
            String MAC,
            String token,
            Timestamp tokenRefreshedAt,
            @Nullable Timestamp lastRefresh,
            @Nullable String name,
            @Nullable Boolean isActive) {
        this.MAC = MAC;
        this.token = token;
        this.tokenRefreshedAt = tokenRefreshedAt;
        this.lastRefresh = lastRefresh;
        this.name = name;
        this.isActive = isActive;
    }

    public Module(DocumentSnapshot snapshot) {
        this.MAC = snapshot.getId();
        this.token = snapshot.getString("token");
        this.name = snapshot.getString("name");
        this.tokenRefreshedAt = snapshot.getTimestamp("tokenRefreshedAt");
        this.lastRefresh = snapshot.getTimestamp("lastRefresh");
        this.createTime = snapshot.getCreateTime();
        this.isActive = snapshot.getBoolean("isActive");
    }

    @Exclude
    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Exclude
    @JsonGetter("tokenRefreshedAt")
    public Long getTokenRefreshedAtInEpoch() {
        return tokenRefreshedAt == null ?
                null :
                tokenRefreshedAt.toDate().getTime();
    }

    @JsonIgnore
    public Timestamp getTokenRefreshedAt() {
        return tokenRefreshedAt;
    }

    public void setTokenRefreshedAt(Timestamp tokenRefreshedAt) {
        this.tokenRefreshedAt = tokenRefreshedAt;
    }

    @Exclude
    @JsonGetter("lastRefresh")
    public Long getLastRefreshInEpoch() {
        return lastRefresh == null ?
                null :
                lastRefresh.toDate().getTime();
    }

    @Nullable
    @JsonIgnore
    public Timestamp getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(@Nullable Timestamp lastRefresh) {
        this.lastRefresh = lastRefresh;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    public Boolean getActive() {
        return isActive;
    }

    public void setActive(@Nullable Boolean active) {
        isActive = active;
    }

    @Exclude
    public Timestamp getCreateTime() {
        return createTime;
    }
}
