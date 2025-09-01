package top.catnies.firitemzoom.render;

import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.render.state.special.SpecialGuiElementRenderState;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record ZoomedItemRenderState(
        ItemStack stack,
        int x,
        int y,
        int size
) implements SpecialGuiElementRenderState {

    @Override
    public int x1() { return x; }

    @Override
    public int y1() { return y; }

    @Override
    public int x2() { return x + size; }

    @Override
    public int y2() { return y + size; }

    @Override
    public float scale() { return (float)size; }

    @Nullable
    @Override
    public ScreenRect scissorArea() { return null; }

    @Nullable
    @Override
    public ScreenRect bounds() {
        return SpecialGuiElementRenderState.createBounds(x1(), y1(), x2(), y2(), scissorArea());
    }
}