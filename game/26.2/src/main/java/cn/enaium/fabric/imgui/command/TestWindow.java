package cn.enaium.fabric.imgui.command;

import cn.enaium.fabric.imgui.ImGuiRenderable;
import cn.enaium.fabric.imgui.TextureBindings;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.AddressMode;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuSampler;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiWindowFlags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.data.AtlasIds;
import net.minecraft.network.chat.Component;

import java.util.OptionalDouble;

public class TestWindow extends Screen implements ImGuiRenderable {
    private static final float BASE_SIZE = 4096;
    private static final float MIN_ZOOM = 0.05f;
    private static final float MAX_ZOOM = 10f;

    private final GpuSampler sampler = RenderSystem.getDevice().createSampler(
            AddressMode.CLAMP_TO_EDGE, AddressMode.CLAMP_TO_EDGE,
            FilterMode.NEAREST, FilterMode.NEAREST,
            1, OptionalDouble.empty()
    );

    private float zoom = 1f;

    protected TestWindow(Component title) {
        super(title);
    }

    @Override
    public void render(ImGuiIO io) {
        ImGui.begin("ImGui TestWindow");

        Minecraft minecraft = Minecraft.getInstance();
        TextureAtlas atlas = minecraft.getAtlasManager().getAtlasOrThrow(AtlasIds.BLOCKS);

        ImGui.beginChild("ImageRegion", 0, 0, false, ImGuiWindowFlags.HorizontalScrollbar);

        float wheel = io.getMouseWheel();
        if (ImGui.isWindowHovered() && wheel != 0) {
            float prevZoom = zoom;
            zoom = Math.clamp(zoom * (float) Math.pow(1.1, wheel), MIN_ZOOM, MAX_ZOOM);

            float mouseX = ImGui.getMousePosX() - ImGui.getWindowPosX();
            float mouseY = ImGui.getMousePosY() - ImGui.getWindowPosY();
            float contentX = mouseX + ImGui.getScrollX();
            float contentY = mouseY + ImGui.getScrollY();

            float ratio = zoom / prevZoom;
            ImGui.setScrollX(contentX * ratio - mouseX);
            ImGui.setScrollY(contentY * ratio - mouseY);
        }

        float size = BASE_SIZE * zoom;
        ImGui.image(TextureBindings.INSTANCE.textureId(atlas.getTextureView(), sampler), size, size);

        ImGui.endChild();

        ImGui.end();
    }
}
