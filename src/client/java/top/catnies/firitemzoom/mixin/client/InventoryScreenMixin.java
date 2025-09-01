package top.catnies.firitemzoom.mixin.client;

import top.catnies.firitemzoom.render.ZoomRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public class InventoryScreenMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void onSurvivalRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ZoomRenderer.render(context, mouseX, mouseY);
    }
}
