package ua.kiev.prog.oauth2.loginviagoogle.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Component
@Slf4j
public class GoogleTokenVerifier {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    private GoogleIdTokenVerifier verifier;

    @PostConstruct
    public void init() throws GeneralSecurityException, IOException {
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        verifier = new GoogleIdTokenVerifier.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory)
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    public GoogleIdToken.Payload verify(String idTokenString) throws GeneralSecurityException, IOException {
        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) {
            return idToken.getPayload();
        } else {
            log.error("Invalid ID token.");
            return null;
        }
    }
}