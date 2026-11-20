package com.syn.visualmod;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ClickGui extends Screen {
    protected ClickGui() { super(Text.literal("Arlakenti v3")); }

    @Override
    protected void init() {
        this.addDrawableChild(ButtonWidget.builder(Text.literal("TargetHUD: " + (VisualMod.targetHud ? "ON" : "OFF")), btn -> {
            VisualMod.targetHud = !VisualMod.targetHud; btn.setMessage(Text.literal("TargetHUD: " + (VisualMod.targetHud ? "ON" : "OFF")));
        }).dimensions(20, 40, 120, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("ArmorHUD: " + (VisualMod.armorHud ? "ON" : "OFF")), btn -> {
            VisualMod.armorHud = !VisualMod.armorHud; btn.setMessage(Text.literal("ArmorHUD: " + (VisualMod.armorHud ? "ON" : "OFF")));
        }).dimensions(20, 70, 120, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Crosshair: " + (VisualMod.customCrosshair ? "ON" : "OFF")), btn -> {
            VisualMod.customCrosshair = !VisualMod.customCrosshair; btn.setMessage(Text.literal("Crosshair: " + (VisualMod.customCrosshair ? "ON" : "OFF")));
        }).dimensions(20, 100, 120, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("PotionHUD: " + (VisualMod.potionHud ? "ON" : "OFF")), btn -> {
            VisualMod.potionHud = !VisualMod.potionHud; btn.setMessage(Text.literal("PotionHUD: " + (VisualMod.potionHud ? "ON" : "OFF")));
        }).dimensions(20, 130, 120, 20).build());
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
