package com.signalist.stock.util;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GeminiClient {

    private final Client client;
    @Value("${gemini.model}")
    private String model;

    public GeminiClient(Client client){
        this.client = client;
    }

    public String generate(String prompt){
        try{
            GenerateContentResponse response = client.models.generateContent(model,prompt,null);
            return response.text();
        }catch(Exception ex){
            throw new RuntimeException("Gemini generation failed ",ex);
        }
    }
}
