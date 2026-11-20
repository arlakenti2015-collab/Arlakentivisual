package com.syn.visualmod;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ClickGui extends Screen {
    public ClickGui() { super(Text.literal("Arlakenti v3")); }

    @Override
    protected void init() {
        int y = 40;
        this.addDrawableChild(ButtonWidget.builder(Text.literal("TargetHUD: " + (VisualMod.targetHud ? "ON" : "OFF")), b -> { VisualMod.targetHud = !VisualMod.targetHud; b.setMessage(Text.literal("TargetHUD: " + (VisualMod.targetHud ? "ON" : "OFF"))); }).dimensions(20, y, 120, 20).build());
        y += 30;
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Crosshair: " + (VisualMod.customCrosshair ? "ON" : "OFF")), b -> { VisualMod.customCrosshair = !VisualMod.customCrosshair; b.setMessage(Text.literal("Crosshair: " + (VisualMod.customCrosshair ? "ON" : "OFF"))); }).dimensions(20, y, 120, 20).build());
        y += 30;
        this.addDrawableChild(ButtonWidget.builder(Text.literal("ArmorHUD: " + (VisualMod.armorHud ? "ON" : "OFF")), b -> { VisualMod.armorHud = !VisualMod.armorHud; b.setMessage(Text.literal("ArmorHUD: " + (VisualMod.armorHud ? "ON" : "OFF"))); }).dimensions(20, y, 120, 20).build());
        y += 30;
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Fullbright: " + (VisualMod.fullbright ? "ON" : "OFF")), b -> { VisualMod.fullbright = !VisualMod.fullbright; b.setMessage(Text.literal("Fullbright: " + (VisualMod.fullbright ? "ON" : "OFF"))); }).dimensions(20, y, 120, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawTextWithShadow(this.textRenderer, "Arlakenti Visual v3", 20, 20, 0x00FFFF);
        super.render(context, mouseX, mouseY, delta);
    }
    @Override
    public boolean shouldPause() { return false; }
}
