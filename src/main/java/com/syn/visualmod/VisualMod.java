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
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Box;
import net.minecraft.client.render.*;
import com.mojang.blaze3d.systems.RenderSystem;
import org.joml.Matrix4f;
import com.syn.visualmod.modules.Module;

public class VisualMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModuleManager.init();
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

            // Прицел (всегда включен)
            context.fill(screenW / 2 - 1, screenH / 2 - 6, screenW / 2 + 1, screenH / 2 + 6, 0xFFFFFFFF);
            context.fill(screenW / 2 - 6, screenH / 2 - 1, screenW / 2 + 6, screenH / 2 + 1, 0xFFFFFFFF);

            // TargetHUD
            Module th = ModuleManager.getModule("TargetHUD");
            if (th != null && th.enabled && client.crosshairTarget instanceof EntityHitResult hit) {
                Entity target = hit.getEntity();
                if (target instanceof LivingEntity living) {
                    context.fill(screenW / 2 - 60, screenH - 70, screenW / 2 + 60, screenH - 30, 0x80000000);
                    context.drawTextWithShadow(client.textRenderer, living.getName().getString(), screenW / 2 - 30, screenH - 65, 0xFF5555);
                    context.drawTextWithShadow(client.textRenderer, "HP: " + living.getHealth(), screenW / 2 - 20, screenH - 50, 0x55FF55);
                }
            }
        });

        WorldRenderEvents.AFTER_ENTITIES.register((context) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.world == null || client.player == null) return;
            MatrixStack matrices = context.matrixStack();
            Vec3d camPos = context.camera().getPos();

            Module hb = ModuleManager.getModule("Hitboxes");
            if (hb != null && hb.enabled) {
                for (PlayerEntity player : client.world.getPlayers()) {
                    if (player == client.player) continue;
                    if (player.squaredDistanceTo(client.player) > 4096) continue;
                    
                    Vec3d pos = player.getPos().subtract(camPos);
                    Box box = player.getBoundingBox().offset(-player.getX() + pos.x, -player.getY() + pos.y, -player.getZ() + pos.z);
                    
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    RenderSystem.disableDepthTest();
                    RenderSystem.setShader(GameRenderer::getPositionColorProgram);
                    Tessellator tess = Tessellator.getInstance();
                    BufferBuilder buffer = tess.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
                    Matrix4f matrix = matrices.peek().getPositionMatrix();
                    buffer.vertex(matrix, (float)box.minX, (float)box.minY, (float)box.minZ).color(255, 0, 0, 255);
                    buffer.vertex(matrix, (float)box.maxX, (float)box.minY, (float)box.minZ).color(255, 0, 0, 255);
                    BufferRenderer.drawWithGlobalProgram(buffer.end());
                    RenderSystem.enableDepthTest();
                    RenderSystem.disableBlend();
                }
            }
        });
    }
}
