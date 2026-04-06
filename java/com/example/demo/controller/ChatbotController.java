package com.example.demo.controller;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.io.InputStream;
import java.util.*;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin("*")
public class ChatbotController {

    private static final String PROJECT_ID = "emsbot-qpqu";

    @PostMapping("/ask")
    public Map<String, String> ask(@RequestBody Map<String, String> body,
                                   HttpSession session) {
        try {
            String userMessage = body.get("message");
            String sessionId   = body.getOrDefault("sessionId",
                                 UUID.randomUUID().toString());

            // ✅ JSON key load karo
            InputStream credStream = new ClassPathResource(
                "emsbot-qpqu-75e5ae5cb91e.json").getInputStream();

            GoogleCredentials credentials = GoogleCredentials
                .fromStream(credStream)
                .createScoped(
                    "https://www.googleapis.com/auth/cloud-platform");

            SessionsSettings settings = SessionsSettings.newBuilder()
                .setCredentialsProvider(
                    FixedCredentialsProvider.create(credentials))
                .build();

            // ✅ Dialogflow se baat karo
            try (SessionsClient sessionsClient =
                     SessionsClient.create(settings)) {

                SessionName sessionName =
                    SessionName.of(PROJECT_ID, sessionId);

                TextInput textInput = TextInput.newBuilder()
                    .setText(userMessage)
                    .setLanguageCode("en")
                    .build();

                QueryInput queryInput = QueryInput.newBuilder()
                    .setText(textInput)
                    .build();

                DetectIntentResponse response =
                    sessionsClient.detectIntent(sessionName, queryInput);

                String reply = response.getQueryResult()
                                       .getFulfillmentText();

                if (reply == null || reply.isEmpty()) {
                    reply = "Sorry, I didn't understand. " +
                            "Please ask about attendance, salary or leaves! 😊";
                }

                return Map.of("reply", reply, "sessionId", sessionId);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("reply",
                "Sorry, main abhi available nahi hoon. " +
                "Thodi der baad try karo! 🙏");
        }
    }
}