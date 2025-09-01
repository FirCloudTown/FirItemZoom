package top.catnies.firitemzoom.mixin.client;

import com.google.common.collect.ImmutableList;
import top.catnies.firitemzoom.render.ZoomedItemGuiElementRenderer;
import net.minecraft.client.gui.render.SpecialGuiElementRenderer;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.ArrayList;
import java.util.List;

/**
 * 注册自定义的 GUI 元素渲染器
 */
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    /**
     * 创建一个访问器来获取 GameRenderer 中的私有字段 buffers
     * @return 返回 BufferBuilderStorage 实例。
     */
    @Accessor("buffers")
    public abstract BufferBuilderStorage getBuffers();

    /**
     * @param originalRenderers 原始的 SpecialGuiElementRenderer 列表
     * @return 自定义渲染器的新列表
     */
    @ModifyArg(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/render/GuiRenderer;<init>(Lnet/minecraft/client/gui/render/state/GuiRenderState;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Ljava/util/List;)V"
            ),
            index = 2
    )
    private List<SpecialGuiElementRenderer<?>> addCustomGuiRenderer(List<SpecialGuiElementRenderer<?>> originalRenderers) {
        // 创建一个可变列表，并复制原始渲染器的所有内容
        List<SpecialGuiElementRenderer<?>> newRenderers = new ArrayList<>(originalRenderers);

        // 通过之前定义的访问器获取 BufferBuilderStorage
        BufferBuilderStorage bufferStorage = this.getBuffers();

        // 创建 ZoomedItemGuiElementRenderer 实例，并传入渲染实体所需的顶点消费者
        // 将其添加到新的渲染器列表中
        newRenderers.add(new ZoomedItemGuiElementRenderer(bufferStorage.getEntityVertexConsumers()));

        // 返回一个不可变列表
        return ImmutableList.copyOf(newRenderers);
    }
}
