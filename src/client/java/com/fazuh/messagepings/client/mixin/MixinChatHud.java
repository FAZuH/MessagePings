package com.fazuh.messagepings.client.mixin;


import com.fazuh.messagepings.client.MessagepingsClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.Instant;

@Mixin(ChatHud.class)
public class MixinChatHud {
    @Unique
    private static String lastMessage;

    @Inject(method = "addMessage(Lnet/minecraft/client/gui/hud/ChatHudLine;)V", at = @At("HEAD"))
    private void onAddMessage(ChatHudLine message, CallbackInfo ci) {
        MessagepingsClient messagePings = MessagepingsClient.getInstance();
        String messageContent = message.content().getString();

        if (messageContent.equals(lastMessage)) {
            return;
        }
        lastMessage = messageContent;

        if (messagePings != null) {
            new Thread(() -> {
                long timestamp = Instant.now().getEpochSecond();
                messagePings.getCore().getMessageHandler().onMessage(messageContent, timestamp);
            }).start();
        }
    }
}
