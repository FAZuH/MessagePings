package com.fazuh.messagepings.client.config;

import java.net.URI;
import java.util.regex.Pattern;

public class MessagePingEntry {
    private Pattern messagePattern;
    private URI uri;
    private String pingFormat;

    public MessagePingEntry(Pattern messagePattern, URI uri, String pingFormat) {
        if (messagePattern == null) {
            throw new IllegalArgumentException("messagePattern cannot be null");
        }
        if (uri == null) {
            throw new IllegalArgumentException("uri cannot be null");
        }
        if (pingFormat == null) {
            throw new IllegalArgumentException("pingFormat cannot be null");
        }
        this.messagePattern = messagePattern;
        this.uri = uri;
        this.pingFormat = pingFormat;
    }

    public MessagePingEntry(String messagePattern, String uri, String pingFormat) {
        this(Pattern.compile(messagePattern), URI.create(uri), pingFormat);
    }

    public Pattern getMessagePattern() {
        return messagePattern;
    }

    public URI getUri() {
        return uri;
    }

    public String getPingFormat() {
        return pingFormat;
    }

    public void setMessagePattern(Pattern messagePattern) {
        this.messagePattern = messagePattern;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public void setPingFormat(String pingFormat) {
        this.pingFormat = pingFormat;
    }
}
