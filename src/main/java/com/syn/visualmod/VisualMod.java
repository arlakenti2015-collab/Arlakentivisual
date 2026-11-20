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
import net.minecraft.item.ItemStack;
import net.minecraft.entity.effect.StatusEffectInstance;

public class VisualMod implements ClientModInitializer {
    public static boolean targetHud = true;
    public static boolean customCrosshair = true;
    public static boolean armorHud = true;
    public static boolean potionHud = true;

    @Override
    public void onInitializeClient() {
        // Открытие меню на 0
        KeyBinding menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.arlakenti.menu", InputUtil.Type.KEYSYM, 48, "category.arlakenti"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (menuKey.wasPressed()) {
                client.setScreen(new ClickGui());
            }
        });

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

            // ArmorHUD
            if (armorHud) {
                int yOffset = 40;
                for (ItemStack stack : client.player.getArmorItems()) {
                    if (!stack.isEmpty()) {
                        context.drawItem(stack, screenW - 30, screenH - yOffset);
                        context.drawItemInSlot(client.textRenderer, stack, screenW - 30, screenH - yOffset);
                        yOffset += 20;
                    }
                }
            }

            // PotionHUD
            if (potionHud) {
                int yOffset = 40;
                for (StatusEffectInstance effect : client.player.getStatusEffects()) {
                    String name = effect.getEffectType().value().getName().getString();
                    int duration = effect.getDuration() / 20;
                    context.drawTextWithShadow(client.textRenderer, name + " " + duration + "s", 10, yOffset, 0xFFFFFF);
                    yOffset += 12;
                }
            }

            // TargetHUD
            if (targetHud && client.crosshairTarget instanceof EntityHitResult hit) {
                Entity target = hit.getEntity();
                if (target instanceof LivingEntity living) {
                    context.fill(screenW / 2 - 60, screenH - 70, screenW / 2 + 60, screenH - 30, 0x80000000);
                    context.drawTextWithShadow(client.textRenderer, living.getName().getString(), screenW / 2 - 30, screenH - 65, 0xFF5555);
                    context.drawTextWithShadow(client.textRenderer, "HP: " + living.getHealth(), screenW / 2 - 20, screenH - 50, 0x55FF55);
                }
            }
        });
    }
}
