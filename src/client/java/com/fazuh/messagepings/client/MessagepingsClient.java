package com.fazuh.messagepings.client;

import com.fazuh.messagepings.client.core.Core;
import net.fabricmc.api.ClientModInitializer;

import java.util.logging.Logger;

public class MessagepingsClient implements ClientModInitializer {
    public static final String MOD_ID = "messagepings";
    public static final String VERSION = "1.0-SNAPSHOT";
    public static final Logger LOGGER = Logger.getLogger(MOD_ID);

    private final Core core = new Core();

    private static MessagepingsClient INSTANCE;

    @Override
    public void onInitializeClient() {
        INSTANCE = this;
    }

    public static MessagepingsClient getInstance() {
        return INSTANCE;
    }

    public Core getCore() {
        return core;
    }
}
