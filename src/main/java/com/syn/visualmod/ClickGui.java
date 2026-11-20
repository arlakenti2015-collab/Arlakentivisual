package com.syn.visualmod;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import com.syn.visualmod.modules.Module;

public class ClickGui extends Screen {
    public ClickGui() { super(Text.literal("Arlakenti v3")); }

    @Override
    protected void init() {
        int y = 40;
        for (Module m : ModuleManager.modules) {
            this.addDrawableChild(ButtonWidget.builder(Text.literal(m.name + ": " + (m.enabled ? "ON" : "OFF")), b -> {
                m.toggle();
                b.setMessage(Text.literal(m.name + ": " + (m.enabled ? "ON" : "OFF")));
            }).dimensions(20, y, 140, 20).build());
            y += 30;
        }
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
