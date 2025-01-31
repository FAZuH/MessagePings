package com.fazuh.messagepings.client.webhook;

import java.net.URI;

public record WebhookPayload(URI targetUrl, String message) { }
