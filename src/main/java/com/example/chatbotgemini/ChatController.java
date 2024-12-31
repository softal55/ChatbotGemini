package com.example.chatbotgemini;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

public class ChatController {
    @FXML
    private TextArea chatArea;
    @FXML
    private TextField inputField;

    private final String geminiApiKey = "AIzaSyA58E_XGEJm5kNnkB0xLvH0XCYM1xrZX2M"; // Replace with your actual key
    private final String geminiApiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + geminiApiKey;

    @FXML
    protected void onSendButtonClick() throws IOException {
        String userMessage = inputField.getText();
        chatArea.appendText("You: " + userMessage + "\n");
        inputField.clear();

        String geminiResponse = getGeminiResponse(userMessage);
        chatArea.appendText("Chatbot: " + geminiResponse + "\n");
    }


    private String getGeminiResponse(String prompt) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(geminiApiUrl);
            request.addHeader("Content-Type", "application/json");

            // Create the JSON structure for the request
            JsonObject json = new JsonObject();
            JsonObject partsJson = new JsonObject();
            partsJson.addProperty("text", prompt);

            JsonObject contentsJson = new JsonObject();
            contentsJson.add("parts", partsJson);

            json.add("contents", contentsJson);

            // Set the JSON as the request entity
            StringEntity params = new StringEntity(json.toString());
            request.setEntity(params);

            // Execute the request and process the response
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String jsonResponse = EntityUtils.toString(response.getEntity(), "UTF-8");
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);

                try {
                    return jsonObject.getAsJsonArray("candidates")
                            .get(0).getAsJsonObject()
                            .getAsJsonObject("content")
                            .getAsJsonArray("parts")
                            .get(0).getAsJsonObject()
                            .get("text").getAsString();

                } catch (Exception e) {
                    System.err.println("Error parsing Gemini response: " + jsonResponse);
                    e.printStackTrace();
                    return "Error getting response from Gemini."; // Handle error as needed
                }
            }
        }
    }

}