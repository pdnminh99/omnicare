package com.thcntt3.omnicare.application;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
public class ApplicationService {
    private final Map<String, Object> logs = Maps.newHashMap();

    private final DocumentReference applicationRef;

    private final CollectionReference applicationCollection;

    private final boolean isDeploymentEnvironment = Boolean.parseBoolean(System.getenv("envi"));

    public ApplicationService(@Qualifier("applicationsCollection") CollectionReference applicationCollection) {
        this.applicationCollection = applicationCollection;
        applicationRef = applicationCollection.document(isDeploymentEnvironment ? "v1" : "dev");
    }

    public void refresh(Timestamp customTime) {
        customTime = Objects.requireNonNullElse(customTime, Timestamp.now());
        logs.put("refreshedAt", customTime);
        applicationRef.set(logs);
    }
}
