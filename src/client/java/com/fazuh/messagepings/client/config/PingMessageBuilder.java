package com.fazuh.messagepings.client.config;

public class PingMessageBuilder {
    private String pingFormat;

    public PingMessageBuilder(String pingFormat) {
        this.pingFormat = pingFormat;
    }

    public PingMessageBuilder setTimestamp(long timestamp) {
        pingFormat = pingFormat.replace("%t", String.valueOf(timestamp));
        return this;
    }

    public PingMessageBuilder setMessage(String message) {
        pingFormat = pingFormat.replace("%s", message);
        return this;
    }

    public String build() {
        return pingFormat;
    }

    public String getPingFormat() {
        return pingFormat;
    }
}
