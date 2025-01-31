package com.fazuh.messagepings.client.core;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public final class FabricUtil {
    public static void sendErrorMessage(String message) {
        sendPlayerWithColor(message, Formatting.RED);
    }

    public static void sendInfoMessage(String message) {
        sendPlayerWithColor(message, Formatting.YELLOW);
    }

    public static void sendSuccessMessage(String message) {
        sendPlayerWithColor(message, Formatting.GREEN);
    }

    private static void sendPlayerWithColor(String message, Formatting color) {
        Text txt = Text.literal(message).withColor(color.getColorValue());
        getPlayer().sendMessage(txt, false);
    }

    public static ClientPlayerEntity getPlayer() {
        return MinecraftClient.getInstance().player;
    }
}
