package com.fazuh.messagepings.client.core;

import com.fazuh.messagepings.client.config.Config;
import com.fazuh.messagepings.client.config.MessagePingEntry;
import com.fazuh.messagepings.client.config.PingMessageBuilder;
import com.fazuh.messagepings.client.webhook.WebhookPayload;

import java.io.IOException;
import java.util.List;

public class MessageEventHandler {
    private final Core core;

    public MessageEventHandler(Core core) {
        this.core = core;
    }

    public void onMessage(String message, long timestamp) {
        if (message == null) {
            return;
        }

        List<MessagePingEntry> entries = getConfig().getEntriesFromString(message);
        for (MessagePingEntry entry : entries) {
            String formattedMessage = new PingMessageBuilder(entry.getPingFormat()).setMessage(message).setTimestamp(timestamp).build();
            WebhookPayload payload = new WebhookPayload(entry.getUri(), formattedMessage);

            try {
                this.core.getWebhook().sendPayload(payload);
            } catch (IOException | InterruptedException e) {
                String err = e.getMessage();
                FabricUtil.sendErrorMessage(err);
            }
        }
    }

    private Config getConfig() {
        return core.getConfig();
    }
}
