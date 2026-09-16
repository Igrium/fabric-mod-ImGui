/*
 * Copyright 2026 Enaium
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.enaium.fabric.imgui;

import com.mojang.blaze3d.textures.GpuTexture;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.GlTexture;

/**
 * Handles mapping textures to longs so they can be passed around in imgui.
 *
 * @author Enaium
 */
public enum TextureBindings {
    INSTANCE;

    /**
     * Get the id to hand to ImGui for a Minecraft texture. ImGui's OpenGL backend
     * binds textures by their GL name, so the GL id of the texture is what it
     * expects. The texture must be uploaded before it is drawn.
     *
     * @param texture texture to use
     * @return the ImGui texture id
     * @throws IllegalStateException when the texture has no OpenGL texture behind it
     */
    public long textureId(AbstractTexture texture) {
        final GpuTexture glTexture = texture.getGlTexture();
        if (!(glTexture instanceof GlTexture)) {
            throw new IllegalStateException("Texture has no OpenGL texture, upload it before drawing it");
        }
        return ((GlTexture) glTexture).getGlId();
    }
}
