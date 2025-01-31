package com.fazuh.messagepings.client.core;

import com.fazuh.messagepings.client.command.PatternCommand;
import com.fazuh.messagepings.client.config.Config;
import com.fazuh.messagepings.client.webhook.DiscordWebhook;

public final class Core {
    private final Config config;
    private final DiscordWebhook webhook;
    private final MessageHandler messageHandler;
    private final PatternCommand patternCommand;

    public Core() {
        config = new Config();
        config.load();
        webhook = new DiscordWebhook();
        messageHandler = new MessageHandler(this);
        patternCommand = new PatternCommand(this);
    }

    public void start() { }

    public void stop() { }

    public DiscordWebhook getWebhook() {
        return webhook;
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public PatternCommand getPatternCommand() { return patternCommand; }

    public Config getConfig() {
        return config;
    }
}
