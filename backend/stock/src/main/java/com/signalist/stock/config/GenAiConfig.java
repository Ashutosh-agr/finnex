package com.signalist.stock.config;

import com.google.genai.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GenAiConfig {

    @Value("${gemini.apiKey}")
    private String googleApiKey;

    @Bean
    public Client genAiClient() {
        // If key provided in application.yml, expose it as a system property so the library can pick it up.
        if (googleApiKey != null && !googleApiKey.isBlank()) {
            System.setProperty("GOOGLE_API_KEY", googleApiKey);
        }

        String effective = System.getenv("GOOGLE_API_KEY");
        if ((effective == null || effective.isBlank()) && (googleApiKey == null || googleApiKey.isBlank())) {
            throw new IllegalStateException("Google API key not set. Add `google.api.key` to application.yml or set env var `GOOGLE_API_KEY`.");
        }

        return new Client();
    }
}
