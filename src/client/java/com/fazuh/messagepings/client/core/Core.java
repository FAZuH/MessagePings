package com.fazuh.messagepings.client.core;

import com.fazuh.messagepings.client.command.PatternCommand;
import com.fazuh.messagepings.client.config.Config;
import com.fazuh.messagepings.client.webhook.DiscordWebhook;

public final class Core {
    private final Config config;
    private final DiscordWebhook webhook;
    private final MessageEventHandler messageEventHandler;
    private final PatternCommand patternCommand;

    public Core() {
        config = new Config();
        config.load();
        webhook = new DiscordWebhook();
        messageEventHandler = new MessageEventHandler(this);
        patternCommand = new PatternCommand(this);
    }

    public void start() {
    }

    public void stop() {
    }

    public DiscordWebhook getWebhook() {
        return webhook;
    }

    public MessageEventHandler getMessageEventHandler() {
        return messageEventHandler;
    }

    public PatternCommand getPatternCommand() {
        return patternCommand;
    }

    public Config getConfig() {
        return config;
    }
}
