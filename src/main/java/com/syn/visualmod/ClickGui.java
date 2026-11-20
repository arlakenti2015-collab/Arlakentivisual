package com.syn.visualmod;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ClickGui extends Screen {
    public ClickGui() { super(Text.literal("Arlakenti v3")); }

    @Override
    protected void init() {
        ButtonWidget btnTarget = ButtonWidget.builder(Text.literal("TargetHUD: " + (VisualMod.targetHud ? "ON" : "OFF")), new ButtonWidget.PressAction() {
            @Override
            public void onPress(ButtonWidget button) {
                VisualMod.targetHud = !VisualMod.targetHud;
                button.setMessage(Text.literal("TargetHUD: " + (VisualMod.targetHud ? "ON" : "OFF")));
            }
        }).dimensions(20, 40, 120, 20).build();
        this.addDrawableChild(btnTarget);

        ButtonWidget btnCross = ButtonWidget.builder(Text.literal("Crosshair: " + (VisualMod.customCrosshair ? "ON" : "OFF")), new ButtonWidget.PressAction() {
            @Override
            public void onPress(ButtonWidget button) {
                VisualMod.customCrosshair = !VisualMod.customCrosshair;
                button.setMessage(Text.literal("Crosshair: " + (VisualMod.customCrosshair ? "ON" : "OFF")));
            }
        }).dimensions(20, 70, 120, 20).build();
        this.addDrawableChild(btnCross);
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
