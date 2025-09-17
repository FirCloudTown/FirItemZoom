package top.catnies.firitemzoom.mixin.client;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HandledScreen.class)
public interface HandledScreenAccessor {
    @Accessor("x")
    int getX();

    @Accessor("y")
    int getY();

    /**
     * 创建一个访问器以获取 'focusedSlot' 字段，它代表当前鼠标悬停或键盘选中的物品槽
     * @return 当前聚焦的 Slot 对象，如果没有则为 null
     */
    @Accessor("focusedSlot")
    @Nullable
    Slot getFocusedSlot();
}
