package com.thcntt3.omnicare.module;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.common.collect.Maps;
import com.google.firebase.messaging.FirebaseMessaging;
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

    private final FirebaseMessaging messaging;

    @Autowired
    ModuleRepository(
            Firestore firestore,
            FirebaseMessaging messaging,
            @Qualifier("modulesCollection") CollectionReference modulesCollection,
            @Qualifier("componentsCollection") CollectionReference componentsCollection) {
        this.firestore = firestore;
        this.messaging = messaging;
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
        return modulesCollection.document(MAC)
                .get()
                .get()
                .toObject(Module.class);
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
        updateMap.put("isActive", true);

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
        WriteBatch temp = firestore.batch();

        var now = Timestamp.now();
        List<DocumentReference> componentsToDelete = componentsCollection
                .whereLessThan("lastRefresh", durationBefore)
                .whereEqualTo("isActive", true)
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(DocumentSnapshot::getReference)
                .collect(Collectors.toList());
        Map<String, Object> updateMap = Maps.newHashMap();
        updateMap.put("lastRefresh", now);
        updateMap.put("isActive", false);

        componentsToDelete.stream()
                .map(DocumentReference::getId)
                .forEach(id -> temp.update(componentsCollection.document(id), updateMap));
        updateMap.clear();
        updateMap.put("lastRefresh", now);

        componentsToDelete.stream()
                .map(DocumentReference::getId)
                .map(id -> id.split("_")[1])
                .forEach(id -> temp.update(modulesCollection.document(id), updateMap));
        temp.commit();
    }

    public void create(Module newModule) {
        modulesCollection.document(newModule.getMAC())
                .set(newModule);
    }

    public Component getComponent(String componentId) throws ExecutionException, InterruptedException {
        return componentsCollection.document(componentId)
                .get()
                .get()
                .toObject(Component.class);
    }

//    public void sendNotification(Module module) throws JsonProcessingException, FirebaseMessagingException {
//        var mapper = new ObjectMapper();
//
//        Message.Builder builder = Message.builder();
//        builder.setTopic(module.getMAC());
//        builder.putData("components", mapper.writeValueAsString(module.getComponents()));
//        builder.putData("name", module.getName());
//
//        if (module.getLastRefresh() != null) {
//            builder.putData("lastRefresh", String.valueOf(module.getLastRefresh().toDate().getTime()));
//        }
//        if (module.getIsActive() != null) {
//            builder.putData("isActive", module.getIsActive().toString());
//        }
//        Message message = builder
//                .build();
//        messaging.send(message);
//    }

    public List<Module> findModuleConnected() throws ExecutionException, InterruptedException {
        return modulesCollection
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(d -> d.toObject(Module.class))
                .collect(Collectors.toList());
    }
}
