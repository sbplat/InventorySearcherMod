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

public class CommandToggleSearchLore {
    public static String getCommandName() {
        return "togglesearchlore";
    }

    public static List<String> getCommandAliases() {
        List<String> aliases = new ArrayList<String>();
        aliases.add("setsearchlore");
        aliases.add("searchlore");
        return aliases;
    }

    public static void setSearchLore(boolean value) {
        InventorySearcher.INSTANCE.getConfig().setSearchLore(value);
        Utils.displayModChatMessage("SearchLore set to " + value);
    }

    public static LiteralCommandNode<FabricClientCommandSource> register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        return dispatcher.register(ClientCommandManager.literal(getCommandName())
            .then(ClientCommandManager.argument("value", BoolArgumentType.bool())
                .executes(context -> {
                    boolean value = BoolArgumentType.getBool(context, "value");
                    setSearchLore(value);
                    return 1;
                })
            )
        );
    }
}
