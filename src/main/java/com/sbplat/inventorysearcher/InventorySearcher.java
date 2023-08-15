package com.sbplat.inventorysearcher;

import java.io.*;
import java.net.*;
import java.nio.file.Paths;
import java.util.concurrent.*;

import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffects;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sbplat.inventorysearcher.commands.*;
import com.sbplat.inventorysearcher.configuration.*;
import com.sbplat.inventorysearcher.utils.*;

public class InventorySearcher implements ClientModInitializer {
    public static InventorySearcher INSTANCE;

    private Config config;

    public static String searchQuery = "";

    public static final Logger LOGGER = LoggerFactory.getLogger(InventorySearcher.class);

    @Override
    public void onInitializeClient() {
        INSTANCE = this;

        File configDir = new File(Minecraft.getInstance().gameDirectory, "config");
        config = new Config(Paths.get(configDir.getAbsolutePath(), "inventorysearcher.json").toString());

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            CommandManager.registerCommands(dispatcher);
        });
    }

    public Config getConfig() {
        return config;
    }
}
