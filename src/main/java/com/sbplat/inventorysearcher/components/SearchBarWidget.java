package com.sbplat.inventorysearcher.components;

import java.util.stream.Stream;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import com.sbplat.inventorysearcher.InventorySearcher;

public class SearchBarWidget extends EditBox {
    public SearchBarWidget(int x, int y, int width, int height) {
        super(Minecraft.getInstance().font, x, y, width, height, Component.empty());
        setValue(InventorySearcher.INSTANCE.searchQuery);
        setResponder((text) -> InventorySearcher.INSTANCE.searchQuery = text);
    }

    public boolean matches(ItemStack stack) {
        boolean searchLore = InventorySearcher.INSTANCE.getConfig().getSearchLore();
        for (String query : getQueries()) {
            String name = stack.getHoverName().getString().toLowerCase();
            if (!name.contains(query)) {
                if (!searchLore) return false;
                Stream<String> lore = stack
                    .getTooltipLines(
                        Minecraft.getInstance().player,
                        Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.ADVANCED : TooltipFlag.NORMAL)
                    .stream()
                    .map((component) -> component.getString().toLowerCase());
                if (!lore.anyMatch((line) -> line.contains(query))) return false;
            }
        }
        return true;
    }

    private String[] getQueries() {
        // Split by "&" if not preceded by a backslash.
        String[] queries = getValue().split("(?<!\\\\)&");
        // Trim and unescape "\&" -> "&".
        for (int i = 0; i < queries.length; ++i) {
            queries[i] = queries[i].trim().replaceAll("\\\\&", "&").toLowerCase();
        }
        return queries;
    }
}
