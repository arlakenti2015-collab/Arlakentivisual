package com.syn.visualmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import com.mojang.blaze3d.systems.RenderSystem;
import org.joml.Matrix4f;

public class VisualMod implements ClientModInitializer {
    public static boolean targetHud = true;
    public static boolean armorHud = true;
    public static boolean hitboxes = true;
    public static boolean potionHud = true;
    public static boolean customCrosshair = true;
    public static boolean killEffect = true;

    @Override
    public void onInitializeClient() {
        // Открытие меню на клавишу 0 (код 48)
        KeyBinding menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.arlakenti.menu", InputUtil.Type.KEYSYM, 48, "category.arlakenti"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (menuKey.wasPressed()) {
                client.setScreen(new ClickGui());
            }
        });

        // --- 2D HUD (Визуал) ---
        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;
            int screenW = context.getScaledWindowWidth();
            int screenH = context.getScaledWindowHeight();

            // 1. Кастомный прицел (Крестик)
            if (customCrosshair) {
                context.fill(screenW / 2 - 1, screenH / 2 - 6, screenW / 2 + 1, screenH / 2 + 6, 0xFFFFFFFF);
                context.fill(screenW / 2 - 6, screenH / 2 - 1, screenW / 2 + 6, screenH / 2 + 1, 0xFFFFFFFF);
            }

            // 2. Armor HUD
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

            // 3. Эффекты зелий
            if (potionHud) {
                int yOffset = 40;
                for (StatusEffectInstance effect : client.player.getStatusEffects()) {
                    String name = effect.getEffectType().value().getName().getString();
                    String duration = " " + (effect.getDuration() / 20) + "s";
                    context.drawTextWithShadow(client.textRenderer, name + duration, 10, yOffset, 0xFFFFFF);
                    yOffset += 12;
                }
            }

            // 4. TargetHUD
            if (targetHud && client.crosshairTarget instanceof EntityHitResult hit) {
                Entity target = hit.getEntity();
                if (target instanceof LivingEntity living) {
                    context.fill(screenW / 2 - 60, screenH - 70, screenW / 2 + 60, screenH - 30, 0x80000000);
                    context.drawCenteredTextWithShadow(client.textRenderer, "\u00A7c" + living.getName().getString(), screenW / 2, screenH - 65, 0xFFFFFF);
                    context.drawCenteredTextWithShadow(client.textRenderer, "\u00A7aHP: " + living.getHealth() + " / " + living.getMaxHealth(), screenW / 2, screenH - 50, 0xFFFFFF);
                }
            }
        });

        // --- 3D Рендер (Хитбоксы) ---
        WorldRenderEvents.AFTER_ENTITIES.register((context) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.world == null || client.player == null) return;
            MatrixStack matrices = context.matrixStack();
            Vec3d camPos = context.camera().getPos();

            // Хитбоксы
            if (hitboxes) {
                for (PlayerEntity player : client.world.getPlayers()) {
                    if (player == client.player) continue;
                    if (player.squaredDistanceTo(client.player) > 4096) continue; // Радиус
                    
                    // Отрисовка рамки хитбокса (упрощенно)
                    Vec3d pos = player.getPos().subtract(camPos);
                    Box box = player.getBoundingBox().offset(-player.getX(), -player.getY(), -player.getZ());
                    WorldRenderer.drawBox(matrices, VertexConsumerProvider.immediate(Tessellator.getInstance().begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR)), box, 0, 255, 255, 1);
                }
            }
        });
    }
}
