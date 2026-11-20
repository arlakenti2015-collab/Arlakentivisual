package com.syn.visualmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.EntityHitResult;

public class VisualMod implements ClientModInitializer {
    public static boolean targetHud = true;
    public static boolean customCrosshair = true;

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;
            int screenW = context.getScaledWindowWidth();
            int screenH = context.getScaledWindowHeight();

            // Прицел
            if (customCrosshair) {
                context.fill(screenW / 2 - 1, screenH / 2 - 6, screenW / 2 + 1, screenH / 2 + 6, 0xFFFFFFFF);
                context.fill(screenW / 2 - 6, screenH / 2 - 1, screenW / 2 + 6, screenH / 2 + 1, 0xFFFFFFFF);
            }

            // TargetHUD
            if (targetHud && client.crosshairTarget instanceof EntityHitResult hit) {
                Entity target = hit.getEntity();
                if (target instanceof LivingEntity living) {
                    context.fill(screenW / 2 - 60, screenH - 70, screenW / 2 + 60, screenH - 30, 0x80000000);
                    // Используем drawText вместо drawCenteredTextWithShadow, он есть во всех версиях
                    context.drawTextWithShadow(client.textRenderer, living.getName().getString(), screenW / 2 - 30, screenH - 65, 0xFF5555);
                    context.drawTextWithShadow(client.textRenderer, "HP: " + living.getHealth(), screenW / 2 - 20, screenH - 50, 0x55FF55);
                }
            }
        });
    }
}
