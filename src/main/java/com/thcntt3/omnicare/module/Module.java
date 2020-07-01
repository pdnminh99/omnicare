package com.thcntt3.omnicare.module;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.client.util.Lists;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.Exclude;
import com.google.cloud.firestore.annotation.PropertyName;
import org.springframework.lang.Nullable;

import java.util.List;

public class Module {

    @DocumentId
    private String MAC;

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
    private Boolean isActive;

    @Exclude
    private List<Component> components;

//    private List<String> users;
    // Component:
    // - fire: FIRE; 0 - OFF, 1 - SAFE; 2 - SMOKE; 3 - FIRE
    // - temp-humid: TEMP_HUMID
    // - light: LIGHT

    public Module() {
    }

    public Module(
            String MAC,
            String token,
            Timestamp tokenRefreshedAt,
            @Nullable Timestamp lastRefresh,
            @Nullable String name,
            List<Component> components,
            @Nullable Boolean isActive) {
        this.MAC = MAC;
        this.token = token;
        this.tokenRefreshedAt = tokenRefreshedAt;
        this.lastRefresh = lastRefresh;
        this.name = name;
        this.components = components;
//        this.users = users;
        this.isActive = isActive;
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
    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(@Nullable Boolean active) {
        isActive = active;
    }

    @Exclude
    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public void addComponent(Component component) {
        if (components == null) {
            components = Lists.newArrayList();
        }
        components.add(component);
    }

//    public List<String> getUsers() {
//        return users;
//    }
//
//    public void setUsers(List<String> users) {
//        this.users = users;
//    }
}
