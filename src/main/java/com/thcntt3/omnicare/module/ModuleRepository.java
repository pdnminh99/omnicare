package com.thcntt3.omnicare.module;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Repository
class ModuleRepository {

    private WriteBatch batch;

    private final Firestore firestore;

    private final CollectionReference modulesCollection;

    private final CollectionReference componentsCollection;

    @Autowired
    ModuleRepository(
            Firestore firestore,
            @Qualifier("modulesCollection") CollectionReference modulesCollection,
            @Qualifier("componentsCollection") CollectionReference componentsCollection) {
        this.firestore = firestore;
        this.modulesCollection = modulesCollection;
        this.componentsCollection = componentsCollection;
    }

    private boolean isBatchActive() {
        return batch != null;
    }

    private void resetBatch() {
        batch = firestore.batch();
    }

    public Module getModule(String MAC) throws ExecutionException, InterruptedException {
        DocumentSnapshot snapshot = modulesCollection.document(MAC)
                .get()
                .get();
        if (!snapshot.exists()) {
            return null;
        }
        return new Module(snapshot);
    }

    public void updateModule(Module module) {
        if (!isBatchActive()) {
            resetBatch();
        }
        Map<String, Object> updateMap = Maps.newHashMap();
        updateMap.put("token", module.getToken());
        updateMap.put("tokenRefreshedAt", module.getTokenRefreshedAt());
        updateMap.put("name", module.getName());
        updateMap.put("lastRefresh", module.getLastRefresh());
        updateMap.put("isActive", module.getIsActive());

        batch.update(modulesCollection.document(module.getMAC()), updateMap);
    }

    public void commit() throws ExecutionException, InterruptedException {
        if (isBatchActive()) {
            batch.commit().get();
            batch = null;
        }
    }

    public void commitAsync() {
        if (isBatchActive()) {
            batch.commit();
            batch = null;
        }
    }

    public void setComponent(Component component) {
        if (!isBatchActive()) {
            resetBatch();
        }
        batch.set(componentsCollection.document(component.getComponentId()), component);
    }

    public void removeOldComponents(Timestamp durationBefore) throws ExecutionException, InterruptedException {
        if (!isBatchActive()) {
            resetBatch();
        }
        List<DocumentReference> componentsToDelete = componentsCollection
                .whereLessThan("lastRefresh", durationBefore)
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(DocumentSnapshot::getReference)
                .collect(Collectors.toList());
        Map<String, Object> moduleRefreshMap = Maps.newHashMap();
        moduleRefreshMap.put("lastRefresh", durationBefore);

        componentsToDelete.stream()
                .map(DocumentReference::getId)
                .map(id -> id.split("_")[1])
                .forEach(module -> batch.update(modulesCollection.document(module), moduleRefreshMap));
        componentsToDelete
                .forEach(d -> batch.delete(d));
        commit();
    }

    public void create(Module newModule) {
        modulesCollection.document(newModule.getMAC())
                .set(newModule);
    }
}
