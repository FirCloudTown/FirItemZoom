package top.catnies.firitemzoom.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.render.SpecialGuiElementRenderer;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * 继承自 SpecialGuiElementRenderer，专门用于渲染“放大的物品”这一特殊GUI元素
 */
public class ZoomedItemGuiElementRenderer extends SpecialGuiElementRenderer<ZoomedItemRenderState> {

    private static final Vector3f ROTATION_AXIS = new Vector3f(0.0f, 1.0f, 0.0f);
    private final Quaternionf rotation = new Quaternionf();

    /**
     * @param vertexConsumers 顶点消费者，用于实际的绘制操作
     */
    public ZoomedItemGuiElementRenderer(VertexConsumerProvider.Immediate vertexConsumers) {
        super(vertexConsumers);
    }

    /**
     * @return 返回 ZoomedItemRenderState 的 Class 对象
     */
    @Override
    public Class<ZoomedItemRenderState> getElementClass() {
        return ZoomedItemRenderState.class;
    }

    /**
     * @param state 包含了要渲染物品的所有信息（物品栈、位置、大小等）
     * @param matrices 矩阵堆栈，用于控制变换（位移、旋转、缩放）
     */
    @Override
    protected void render(ZoomedItemRenderState state, MatrixStack matrices) {
        MinecraftClient client = MinecraftClient.getInstance();
        ItemRenderer itemRenderer = client.getItemRenderer();
        var diffuseLighting = client.gameRenderer.getDiffuseLighting();

        // 开启3D物品的光照效果
        diffuseLighting.setShaderLights(DiffuseLighting.Type.ITEMS_3D);

        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));

        long time = System.currentTimeMillis();
        float angle = (time % 5000L) / 5000.0F * 360.0F;

        this.rotation.fromAxisAngleDeg(ROTATION_AXIS, angle);
        matrices.multiply(this.rotation);

        // 调用原版的物品渲染器来绘制物品
        itemRenderer.renderItem(
                state.stack(),
                ItemDisplayContext.GUI,
                0xF000F0,
                OverlayTexture.DEFAULT_UV,
                matrices,
                this.vertexConsumers,
                client.world,
                0
        );

        matrices.pop();

        // 恢复为默认的平面模式
        diffuseLighting.setShaderLights(DiffuseLighting.Type.ITEMS_FLAT);
    }

    /**
     * @return 渲染器名称。
     */
    @Override
    protected String getName() {
        return "fir:zoom_item";
    }

    /**
     * 计算Y轴上的偏移量
     * @param height 屏幕高度
     * @param windowScaleFactor GUI缩放比例
     * @return Y轴偏移量
     */
    @Override
    protected float getYOffset(int height, int windowScaleFactor) {
        return height / 2.0f;
    }
}
