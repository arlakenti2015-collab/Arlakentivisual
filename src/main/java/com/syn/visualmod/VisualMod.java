package com.syn.visualmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.EntityHitResult;

public class VisualMod implements ClientModInitializer {
    public static boolean targetHud = true;
    public static boolean customCrosshair = true;

    @Override
    public void onInitializeClient() {
        KeyBinding menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.arlakenti.menu", InputUtil.Type.KEYSYM, 48, "category.arlakenti"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(new ClientTickEvents.EndTick() {
            @Override
            public void onEndTick(MinecraftClient client) {
                while (menuKey.wasPressed()) {
                    client.setScreen(new ClickGui());
                }
            }
        });

        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;
            int screenW = context.getScaledWindowWidth();
            int screenH = context.getScaledWindowHeight();

            if (customCrosshair) {
                context.fill(screenW / 2 - 1, screenH / 2 - 6, screenW / 2 + 1, screenH / 2 + 6, 0xFFFFFFFF);
                context.fill(screenW / 2 - 6, screenH / 2 - 1, screenW / 2 + 6, screenH / 2 + 1, 0xFFFFFFFF);
            }

            if (targetHud && client.crosshairTarget instanceof EntityHitResult) {
                EntityHitResult hit = (EntityHitResult) client.crosshairTarget;
                Entity target = hit.getEntity();
                if (target instanceof LivingEntity) {
                    LivingEntity living = (LivingEntity) target;
                    context.fill(screenW / 2 - 60, screenH - 70, screenW / 2 + 60, screenH - 30, 0x80000000);
                    context.drawCenteredTextWithShadow(client.textRenderer, "\u00A7c" + living.getName().getString(), screenW / 2, screenH - 65, 0xFFFFFF);
                    context.drawCenteredTextWithShadow(client.textRenderer, "\u00A7aHP: " + living.getHealth(), screenW / 2, screenH - 50, 0xFFFFFF);
                }
            }
        });
    }
}
