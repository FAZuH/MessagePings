package com.fazuh.messagepings.client.config;

import com.fazuh.messagepings.client.MessagepingsClient;
import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class Config {
    private static final String CONFIG_FILE = "messagepings.json";
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Pattern.class, new PatternAdapter())
            .registerTypeAdapter(URI.class, new URIAdapter())
            .create();

    private List<MessagePingEntry> messagePatterns = new ArrayList<>();
    private boolean pingsEnabled = true;

    public void load() {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve(CONFIG_FILE);

        if (!Files.exists(configPath)) {
            messagePatterns = createEmptyConfig();
            save();
            return;
        }

        try (Reader reader = Files.newBufferedReader(configPath)) {
            Config config = GSON.fromJson(reader, Config.class);
            if (config != null && config.messagePatterns != null) {
                this.messagePatterns = config.messagePatterns;
            }
        } catch (IOException | JsonSyntaxException e) {
            MessagepingsClient.LOGGER.warning("Failed to load config: " + e.getMessage());
            messagePatterns = createEmptyConfig();
            save();
        }
    }

    public void save() {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve(CONFIG_FILE);
        try (Writer writer = Files.newBufferedWriter(configPath)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            MessagepingsClient.LOGGER.warning("Failed to save config: " + e.getMessage());
        }
    }

    public boolean isEnabled() {
        return pingsEnabled;
    }
    
    public void togglePings() {
        pingsEnabled = !pingsEnabled;
    }

    public List<MessagePingEntry> getMessagePatterns() {
        return messagePatterns;
    }

    public List<MessagePingEntry> getEntriesFromString(String entry) {
        List<MessagePingEntry> ret = new ArrayList<>();
        for (MessagePingEntry messagePingEntry : messagePatterns) {
            if (messagePingEntry.getMessagePattern().matcher(entry).matches()) {
                ret.add(messagePingEntry);
            }
        }
        return ret;
    }

    private List<MessagePingEntry> createEmptyConfig() {
        return new ArrayList<>();
    }

    // Gson TypeAdapter for Pattern
    private static class PatternAdapter implements JsonSerializer<Pattern>, JsonDeserializer<Pattern> {
        @Override
        public JsonElement serialize(Pattern pattern, Type type, JsonSerializationContext context) {
            return new JsonPrimitive(pattern.pattern());
        }

        @Override
        public Pattern deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            return Pattern.compile(element.getAsString());
        }
    }

    // Gson TypeAdapter for URI
    private static class URIAdapter implements JsonSerializer<URI>, JsonDeserializer<URI> {
        @Override
        public JsonElement serialize(URI uri, Type type, JsonSerializationContext context) {
            return new JsonPrimitive(uri.toString());
        }

        @Override
        public URI deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            return URI.create(element.getAsString());
        }
    }
}
