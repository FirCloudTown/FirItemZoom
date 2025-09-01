package top.catnies.firitemzoom.render;


import top.catnies.firitemzoom.mixin.client.HandledScreenAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

/**
 * 负责处理放大物品渲染
 */
public class ZoomRenderer {

    // 缓存上一次聚焦的物品槽
    private static Slot lastFocusedSlot = null;
    // 缓存上一次的渲染状态对象
    private static ZoomedItemRenderState cachedState = null;
    // 用于检测窗口大小变化，当窗口大小改变时，需要重新计算渲染位置和大小
    private static int lastScreenWidth = 0;
    private static int lastScreenHeight = 0;

    /**
     * 渲染方法，由 HandledScreenMixin 调用
     * @param context 绘图上下文
     * @param mouseX 鼠标X坐标
     * @param mouseY 鼠标Y坐标
     */
    public static void render(DrawContext context, int mouseX, int mouseY) {
        MinecraftClient client = MinecraftClient.getInstance();
        // 当前屏幕是否是 HandledScreen 的实例
        if (!(client.currentScreen instanceof HandledScreen<?> screen)) {
            lastFocusedSlot = null;
            cachedState = null;
            return;
        }

        // 获取当前聚焦的物品槽
        Slot focusedSlot = ((HandledScreenAccessor) screen).getFocusedSlot();
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        // 缓存优化
        if (focusedSlot != null && focusedSlot == lastFocusedSlot
                && cachedState != null && !cachedState.stack().isEmpty()
                && ItemStack.areEqual(cachedState.stack(), focusedSlot.getStack()) // 检查物品是否相同
                && screenWidth == lastScreenWidth && screenHeight == lastScreenHeight) { // 检查窗口大小是否相同
            context.state.addSpecialElement(cachedState);
            return;
        }

        // 任何情况发生变化，都更新缓存
        lastFocusedSlot = focusedSlot;
        lastScreenWidth = screenWidth;
        lastScreenHeight = screenHeight;

        // 检查当前是否有聚焦的槽，并且槽里有物品
        if (focusedSlot != null && focusedSlot.hasStack()) {
            ItemStack stack = focusedSlot.getStack();
            // 获取GUI的左边界位置
            int guiLeft = ((HandledScreenAccessor) screen).getX();

            // 计算放大物品的尺寸，取屏幕宽和高中较小者的 45%
            int size = (int) (Math.min(screenWidth, screenHeight) * 0.45);
            int x;

            // 决定渲染位置
            if (guiLeft >= size + 8) {
                x = (guiLeft - size) / 2;
            } else {
                x = guiLeft - size - 4;
            }
            int y = (screenHeight - size) / 2;

            cachedState = new ZoomedItemRenderState(stack.copy(), x, y, size);
            context.state.addSpecialElement(cachedState);

        } else {
            // 如果没有聚焦的物品，清空缓存
            cachedState = null;
        }
    }
}
