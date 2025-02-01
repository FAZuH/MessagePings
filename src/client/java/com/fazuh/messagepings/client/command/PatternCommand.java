package com.fazuh.messagepings.client.command;

import static com.mojang.brigadier.arguments.StringArgumentType.*;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

import com.fazuh.messagepings.client.config.Config;
import com.fazuh.messagepings.client.config.MessagePingEntry;
import com.fazuh.messagepings.client.core.Core;
import com.fazuh.messagepings.client.core.FabricUtil;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;

import java.util.List;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

public class PatternCommand {
    private static final String BASE_COMMAND = "mp";

    private final Core core;

    public PatternCommand(Core core) {
        this.core = core;
        setup();
    }

    private void setup() {
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess)
                        -> dispatcher.register(
                        literal(BASE_COMMAND)
                                .then(literal("add").then(
                                        argument("pattern", string())
                                                .then(argument("webhook_url", string())
                                                        .then(argument("ping_format", string())
                                                                .executes(this::addPattern)))))
                                .then(literal("remove").then(
                                        argument("pattern", string())
                                                .executes(this::removePattern)))
                                .then(literal("list").executes(this::listPattern))
                                .then(literal("clear").executes(this::clearPatterns))
                                .then(literal("reloadconf").executes(this::reloadConfig))
                                .then(literal("toggle").executes(this::togglePings))));
    }

    private int addPattern(CommandContext<FabricClientCommandSource> context) {
        loadConfig();
        String pattern = getString(context, "pattern");
        String webhookUrl = getString(context, "webhook_url");
        String pingFormat = getString(context, "ping_format");

        try {
            MessagePingEntry newEntry =
                    new MessagePingEntry(pattern, webhookUrl, pingFormat);
            getConfig().getMessagePatterns().add(newEntry);
        } catch (IllegalArgumentException e) {
            FabricUtil.sendErrorMessage("Error adding pattern: " + e.getMessage());
            return -1;
        }

        saveConfig();
        FabricUtil.sendSuccessMessage("Pattern added successfully!");
        return Command.SINGLE_SUCCESS;
    }

    private int removePattern(CommandContext<FabricClientCommandSource> context) {
        loadConfig();
        String pattern = getString(context, "pattern");

        boolean isFound = getConfig().getMessagePatterns().removeIf(
                entry -> entry.getMessagePattern().pattern().equals(pattern));

        if (!isFound) {
            FabricUtil.sendErrorMessage("Pattern not found!");
            return -1;
        }
        saveConfig();
        FabricUtil.sendSuccessMessage("Pattern removed successfully!");
        return Command.SINGLE_SUCCESS;
    }

    private int listPattern(CommandContext<FabricClientCommandSource> context) {
        loadConfig();
        if (getMessagePatterns().isEmpty()) {
            FabricUtil.sendInfoMessage("No patterns found!");
        }
        for (MessagePingEntry entry : getConfig().getMessagePatterns()) {
            String msg = "Pattern '" + entry.getMessagePattern().pattern() +
                    "'\nURL: '" + entry.getUri().getRawPath() +
                    "'\nFormat: '" + entry.getPingFormat() +
                    "'\n---------------------------------";
            FabricUtil.sendInfoMessage(msg);
        }
        return Command.SINGLE_SUCCESS;
    }

    private int clearPatterns(CommandContext<FabricClientCommandSource> context) {
        getMessagePatterns().clear();
        saveConfig();
        FabricUtil.sendSuccessMessage("Patterns cleared successfully!");
        return Command.SINGLE_SUCCESS;
    }

    private int reloadConfig(CommandContext<FabricClientCommandSource> context) {
        loadConfig();
        FabricUtil.sendSuccessMessage("Config reloaded successfully!");
        return Command.SINGLE_SUCCESS;
    }

    private int togglePings(CommandContext<FabricClientCommandSource> context) {
        getConfig().togglePings();
        FabricUtil.sendSuccessMessage("Toggled message pings " +
                (getConfig().isEnabled() ? "on" : "off"));
        saveConfig();
        return Command.SINGLE_SUCCESS;
    }

    private Config getConfig() {
        return core.getConfig();
    }

    private void saveConfig() {
        getConfig().save();
    }

    private void loadConfig() {
        getConfig().load();
    }

    private List<MessagePingEntry> getMessagePatterns() {
        return getConfig().getMessagePatterns();
    }
}
