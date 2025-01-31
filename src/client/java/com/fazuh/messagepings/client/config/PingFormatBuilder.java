package com.fazuh.messagepings.client.config;

public class PingFormatBuilder {
    private String pingFormat;

    public PingFormatBuilder(String pingFormat) {
        this.pingFormat = pingFormat;
    }

    public PingFormatBuilder setTimestamp(long timestamp) {
        pingFormat = pingFormat.replace("%t", String.valueOf(timestamp));
        return this;
    }

    public PingFormatBuilder setMessage(String message) {
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
