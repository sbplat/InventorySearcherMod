package com.sbplat.inventorysearcher.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.tree.LiteralCommandNode;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import com.sbplat.inventorysearcher.InventorySearcher;
import com.sbplat.inventorysearcher.utils.Utils;

public class CommandCoverBorders {
    public static String getCommandName() {
        return "coverborders";
    }

    public static List<String> getCommandAliases() {
        List<String> aliases = new ArrayList<String>();
        aliases.add("setcoverborders");

        return aliases;
    }

    public static void setCoverBorders(boolean value) {
        InventorySearcher.INSTANCE.getConfig().setCoverBorders(value);
        Utils.displayModChatMessage("Cover borders set to " + value);
    }

    public static LiteralCommandNode<FabricClientCommandSource> register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        return dispatcher.register(ClientCommandManager.literal(getCommandName())
            .then(ClientCommandManager.argument("value", BoolArgumentType.bool())
                .executes(context -> {
                    boolean value = BoolArgumentType.getBool(context, "value");
                    setCoverBorders(value);
                    return 1;
                })
            )
        );
    }
}
