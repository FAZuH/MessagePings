package com.fazuh.messagepings.client.core;


import com.fazuh.messagepings.client.MessagepingsClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.Instant;

@Mixin(ChatHud.class)
public class MixinChatHud {
    @Inject(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"))
    private void onAddMessage(Text message, CallbackInfo ci) {
        String msg = message.getString();
        long epochSecond = Instant.now().getEpochSecond();

        MessagepingsClient mpClient = MessagepingsClient.getInstance();
        if (mpClient != null) {
            mpClient.getCore().getMessageHandler().onMessage(msg, epochSecond);
        }
    }
}
