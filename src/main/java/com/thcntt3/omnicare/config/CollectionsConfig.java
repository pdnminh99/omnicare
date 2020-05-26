package com.thcntt3.omnicare.config;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;

import java.util.Optional;

@Configuration
public class CollectionsConfig {

    private final Firestore firestore;

    private final boolean isDeploymentEnvironment = Boolean.parseBoolean(System.getenv("envi"));

    @Autowired
    public CollectionsConfig(Firestore firestore) {
        this.firestore = firestore;
    }

    @Bean("modulesCollection")
    @DependsOn("firestore")
    @Scope("singleton")
    public CollectionReference getUsersCollection() throws DatabaseFailedToInitializeException {
        return Optional.ofNullable(firestore)
                .map(db -> db.collection(isDeploymentEnvironment ? "modules" : "modules_dev"))
                .orElseThrow(DatabaseFailedToInitializeException::new);
    }

    @Bean("componentsCollection")
    @DependsOn("firestore")
    @Scope("singleton")
    public CollectionReference getComponentsCollection() throws DatabaseFailedToInitializeException {
        return Optional.ofNullable(firestore)
                .map(db -> db.collection(isDeploymentEnvironment ? "components" : "components_dev"))
                .orElseThrow(DatabaseFailedToInitializeException::new);
    }

    @Bean("applicationsCollection")
    @DependsOn("firestore")
    @Scope("singleton")
    public CollectionReference getApplicationsCollection() throws DatabaseFailedToInitializeException {
        return Optional.ofNullable(firestore)
                .map(db -> db.collection("application"))
                .orElseThrow(DatabaseFailedToInitializeException::new);
    }
}
