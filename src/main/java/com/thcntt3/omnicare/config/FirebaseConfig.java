package com.thcntt3.omnicare.config;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

@Configuration
public class FirebaseConfig {

    private FirebaseApp app;

    @Bean("firebase")
    public void initializeFirebaseApp() throws IOException {
        if (!FirebaseApp.getApps().isEmpty()) {
            return;
        }

        FirebaseOptions options;
        var env = System.getenv("GOOGLE_CLOUD_PROJECT");

        if (env != null) {
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.getApplicationDefault())
                    .build();
        } else {
            String pathToCredential = Paths.get(".").toAbsolutePath().normalize().toString() + "//key.json";
            InputStream credentialFile = new FileInputStream(pathToCredential);
            GoogleCredentials credentials = GoogleCredentials.fromStream(credentialFile);

            options = new FirebaseOptions.Builder()
                    .setCredentials(credentials)
                    .build();
        }
        app = FirebaseApp.initializeApp(options);
    }

    @Bean("firestore")
    @DependsOn("firebase")
    public Firestore getFirestore() {
        return FirestoreClient.getFirestore();
    }

    @Bean("fcm")
    @DependsOn("firebase")
    public FirebaseMessaging getFCM() {
        return FirebaseMessaging.getInstance();
    }

    @Bean("firebaseAuth")
    @DependsOn("firebase")
    public FirebaseAuth getFireAuth() {
        return FirebaseAuth.getInstance(app);
    }
}
