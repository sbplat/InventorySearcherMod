package com.sbplat.inventorysearcher.mixin;

import java.awt.Color;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.Slot;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.sbplat.inventorysearcher.InventorySearcher;
import com.sbplat.inventorysearcher.components.SearchBarWidget;

@Mixin(AbstractContainerScreen.class)
public abstract class MixinAbstractContainerScreen extends Screen {
    @Shadow protected int imageWidth;
    @Shadow protected int imageHeight;
    @Shadow protected int leftPos;
    @Shadow protected int topPos;

    private SearchBarWidget searchBarWidget;

    protected MixinAbstractContainerScreen(Component title) {
        super(title);
    }

    @Inject(at=@At("TAIL"), method="init()V")
    private void init(CallbackInfo ci) {
        searchBarWidget = new SearchBarWidget(
            leftPos + 1,
            topPos + imageHeight + 10,
            imageWidth - 2,
            20
        );
        this.addRenderableWidget(searchBarWidget);
    }

    @Inject(at=@At("RETURN"), method="renderSlot(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/inventory/Slot;)V")
    private void renderSlot(PoseStack matrices, Slot slot, CallbackInfo ci) {
        if (searchBarWidget == null) return;
        if (!searchBarWidget.matches(slot.getItem())) {
            RenderSystem.disableDepthTest();
            RenderSystem.colorMask(true, true, true, false);
            int delta = InventorySearcher.INSTANCE.getConfig().getCoverBorders() ? 1 : 0;
            Color highlightColor = InventorySearcher.INSTANCE.getConfig().getHighlightColor();
            int highlightAlpha = InventorySearcher.INSTANCE.getConfig().getHighlightAlpha();
            // Format is 0xAARRGGBB.
            int color = (highlightAlpha << 24) | (highlightColor.getRed() << 16) | (highlightColor.getGreen() << 8) | highlightColor.getBlue();
            this.fillGradient(matrices, slot.x - delta, slot.y - delta, slot.x + 16 + delta, slot.y + 16 + delta, color, color, 1);
            RenderSystem.colorMask(true, true, true, true);
            RenderSystem.enableDepthTest();
        }
    }

    @Inject(at=@At("HEAD"), method="keyPressed(III)Z", cancellable=true)
    private void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (searchBarWidget == null) return;
        if (!searchBarWidget.canConsumeInput()) return;
        if (searchBarWidget.keyPressed(keyCode, scanCode, modifiers)) {
            cir.setReturnValue(true);
            return;
        }
        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER || keyCode == GLFW.GLFW_KEY_TAB || keyCode == GLFW.GLFW_KEY_ESCAPE) {
            searchBarWidget.setFocused(false);
        }
        cir.setReturnValue(true);
    }
}
