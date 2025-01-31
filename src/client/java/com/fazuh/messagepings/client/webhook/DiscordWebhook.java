package com.fazuh.messagepings.client.webhook;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fazuh.messagepings.client.MessagepingsClient;

public class DiscordWebhook {
    private static final Gson gson = new Gson();

    public void sendPayload(WebhookPayload payload) throws IOException, InterruptedException {
        String json = gson.toJson(new DiscordMessage(payload.message()));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(payload.targetUrl())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 204) {
                MessagepingsClient.LOGGER.warning("Error sending webhook message: " + response.statusCode() + " - " + response.body());
            } else {
                MessagepingsClient.LOGGER.info("Webhook message sent successfully!");
            }
        }
    }

    private static class DiscordMessage {
        private final String content;

        public DiscordMessage(String content) {
            this.content = content;
        }
    }
}