package com.thcntt3.omnicare.module;

import com.google.cloud.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.thcntt3.omnicare.application.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


@Service
public class ModuleService {

    @Value("${tokenExpireAfter:1}")
    public int tokenExpireAfter;

    @Value("${durationBeforeDisconnect:10}")
    public int durationBeforeDisconnect;

    private final ModuleRepository repository;

    private final ApplicationService applicationService;

    private final FirebaseAuth firebaseAuth;

    @Scheduled(fixedRate = 5000)
    public void check() throws ExecutionException, InterruptedException {
        var calendar = Calendar.getInstance();
        var now = Timestamp.now();
        calendar.setTime(now.toDate());
        calendar.add(Calendar.SECOND, -durationBeforeDisconnect);

        var durationBefore = Timestamp.of(calendar.getTime());
        repository.removeOldComponents(durationBefore);
        applicationService.refresh(now);
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

    public void updateComponent(String token, String MAC, Component abstractComponent) throws ExecutionException, InterruptedException {
        if (abstractComponent.getType() == null) {
            throw new IllegalArgumentException("Unknown type of component.");
        }
        var now = Timestamp.now();
        Module module = repository.getModule(MAC);
        if (module == null) {
            throw new IllegalArgumentException("No module match MAC address [" + MAC + "].");
        }
        assert module.getTokenRefreshedAt() != null;
        double hoursFromLastTokenRefreshed = 1.0 * (now.getSeconds() - module.getTokenRefreshedAt().getSeconds()) / 3600;
        System.out.println("Hours passed " + hoursFromLastTokenRefreshed + ".");
        if (!Objects.equals(module.getToken(), token)) {
            throw new IllegalArgumentException("Token not match.");
        }
        if (hoursFromLastTokenRefreshed > tokenExpireAfter) {
            throw new IllegalArgumentException("Token expired.");
        }
        abstractComponent.setLastRefresh(now);
        repository.setComponent(abstractComponent);
        module.setLastRefresh(now);
        repository.updateModule(module);
        repository.commit();
    }

    public String create(String MAC, String name) throws FirebaseAuthException {
        var now = Timestamp.now();
        var newModule = new Module(MAC, firebaseAuth.createCustomToken(MAC).trim(), now, now, name, true);
        repository.create(newModule);
        return newModule.getToken();
    }
}

