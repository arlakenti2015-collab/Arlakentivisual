package com.syn.visualmod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.EntityHitResult;

public class VisualMod implements ClientModInitializer {
    public static boolean targetHud = true;
    public static boolean fovEnabled = true;
    public static int customFov = 120;
    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null || !targetHud) return;
            context.fill(5, 5, 120, 30, 0x80000000);
            context.drawTextWithShadow(client.textRenderer, "\u00A7bArlakenti Visual", 10, 10, 0xFFFFFF);
            if (client.crosshairTarget instanceof EntityHitResult hit) {
                Entity target = hit.getEntity();
                if (target instanceof LivingEntity living) {
                    int screenW = context.getScaledWindowWidth();
                    int screenH = context.getScaledWindowHeight();
                    context.fill(screenW / 2 - 50, screenH - 60, screenW / 2 + 50, screenH - 30, 0x80000000);
                    context.drawCenteredTextWithShadow(client.textRenderer, "\u00A7c" + living.getName().getString(), screenW / 2, screenH - 55, 0xFFFFFF);
                    context.drawCenteredTextWithShadow(client.textRenderer, "\u00A7aHP: " + living.getHealth(), screenW / 2, screenH - 45, 0xFFFFFF);
                }
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.options != null) {
                client.options.getFov().setValue(fovEnabled ? customFov : 70);
            }
        });
    }
}
