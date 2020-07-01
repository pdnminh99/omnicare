package com.thcntt3.omnicare.module;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.client.util.Lists;
import com.google.cloud.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.thcntt3.omnicare.application.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


@Service
public class ModuleService {

    @Value("${tokenExpireAfter:1}")
    public int tokenExpireAfter;

    @Value("${durationBeforeDisconnect:15}")
    public int durationBeforeDisconnect;

    private final ModuleRepository repository;

    private final ApplicationService applicationService;

    private final FirebaseAuth firebaseAuth;

    @Scheduled(fixedRate = 2500)
    public void check() throws ExecutionException, InterruptedException {
        var calendar = Calendar.getInstance();
        var now = Timestamp.now();
        calendar.setTime(now.toDate());
        calendar.add(Calendar.SECOND, -durationBeforeDisconnect);

        var durationBefore = Timestamp.of(calendar.getTime());
        repository.removeOldComponents(durationBefore);
//        applicationService.refresh(now);
    }

    @Autowired
    public ModuleService(ModuleRepository repository, ApplicationService applicationService, FirebaseAuth firebaseAuth) {
        this.repository = repository;
        this.applicationService = applicationService;
        this.firebaseAuth = firebaseAuth;
    }

    public String getNewToken(String MAC, String name) throws ExecutionException, InterruptedException, FirebaseAuthException {
        Module module = repository.getModule(MAC);
        if (module == null) {
            return create(MAC, name);
        }
        String newToken = firebaseAuth.createCustomToken(MAC).trim();

        module.setToken(newToken);
        module.setTokenRefreshedAt(Timestamp.now());

        repository.updateModule(module);
        repository.commit();
        return newToken;
    }

    private Module module;

    private Component component;

    private Timestamp now;

    public void updateComponent(
            String token,
            String MAC,
            int pinNumber,
            int maximumDataCount,
            RawData data)
            throws ExecutionException, InterruptedException {
        now = Timestamp.now();

        getModule(MAC);
        assert module.getTokenRefreshedAt() != null;
        validateToken(token);
        getComponent(MAC, pinNumber);

        if (component == null) {
            // Create new.
            component = Component.newBuilder()
                    .setMAC(MAC)
                    .setPinNumber(pinNumber)
                    .addData(data)
                    .setLastRefresh(now)
                    .setIsActive(true)
                    .build();
        } else applyUpdates(data, maximumDataCount);
        component.setLastRefresh(now);
        repository.setComponent(component);
        module.addComponent(component);
    }

    public void commit() throws ExecutionException, InterruptedException {
        module.setLastRefresh(now);
        repository.updateModule(module);
        repository.commit();
    }

    public void publishMessage() throws FirebaseMessagingException, JsonProcessingException {
        // repository.sendNotification(module);
    }

    public void flush() {
        component = null;
        module = null;
    }

    private void applyUpdates(RawData data, int maximumDataCount) {
        List<RawData> original = component.getData();
        if (original == null) {
            original = Lists.newArrayList();
        }
        original.add(data);
        original.sort((a, b) -> -a.getCreatedAt().compareTo(b.getCreatedAt()));
        if (original.size() < maximumDataCount) {
            return;
        }
        component.setData(original.subList(0, maximumDataCount));
        component.setActive(true);
    }

    private void getComponent(String MAC, int pinNumber) throws ExecutionException, InterruptedException {
        String componentId = pinNumber + "_" + MAC;

        if (component == null || !component.getComponentId().equals(componentId)) {
            component = repository.getComponent(componentId);
        }
    }

    private void getModule(String MAC) throws ExecutionException, InterruptedException {
        module = repository.getModule(MAC);
        if (module == null) {
            throw new IllegalArgumentException("No module match MAC address [" + MAC + "].");
        }
    }

    private void validateToken(String token) {
        double hoursFromLastTokenRefreshed = 1.0 * (now.getSeconds() - module.getTokenRefreshedAt().getSeconds()) / 3600;

        if (!Objects.equals(module.getToken(), token)) {
            throw new IllegalArgumentException("Token not match.");
        }
        if (hoursFromLastTokenRefreshed > tokenExpireAfter) {
            throw new IllegalArgumentException("Token expired.");
        }
    }

    public String create(String MAC, String name) throws FirebaseAuthException {
        var now = Timestamp.now();
        var newModule = new Module(MAC, firebaseAuth.createCustomToken(MAC).trim(), now, now, name, null, true);
        repository.create(newModule);
        return newModule.getToken();
    }

    public List<Module> getAll() throws ExecutionException, InterruptedException {
        return repository.findModuleConnected();
    }
//
//    public Module connect(String uid, String MAC) {
//        return null;
//    }
//
//    public void disconnect(String uid, String MAC) throws ExecutionException, InterruptedException {
//        module = repository.getModule(MAC);
//        if (module == null) {
//            throw new IllegalAccessError("Module with MAC address [" + MAC + "] not found.");
//        }
//        List<String> users = module.getUsers();
//        if (users == null || users.stream().noneMatch(u -> u.equals(uid))) {
//            throw new IllegalArgumentException("User [" + uid + "] does not have access permission to module [" + MAC + "].");
//        }
//        for (int index = 0; index < users.size(); index++) {
//            if (users.get(index).equals(uid)) {
//                users.remove(index);
//                break;
//            }
//        }
//        repository.updateModule(module);
//        repository.commit();
//        flush();
//    }
}

