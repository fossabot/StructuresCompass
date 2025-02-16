package com.github.samarium150.structurescompass.util;

import com.github.samarium150.structurescompass.config.HUDPosition;
import com.github.samarium150.structurescompass.config.StructuresCompassConfig;
import com.mojang.blaze3d.vertex.*;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

/**
 * Utilities related to rendering
 * <p>
 * This class is adapted from
 * <a href="https://github.com/MattCzyr/NaturesCompass" target="_blank">NaturesCompass</a>
 * which is under
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0" target="_blank">
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License
 * </a>.
 */
@OnlyIn(Dist.CLIENT)
public abstract class RenderUtils {
    
    private static final Minecraft minecraft = Minecraft.getInstance();
    private static final Font fontRenderer = minecraft.font;
    
    private RenderUtils() { }
    
    private static void drawStringLeft(PoseStack matrixStack, String string, int x, int y, int color) {
        fontRenderer.draw(matrixStack, string, x, y, color);
    }
    
    private static void drawStringRight(PoseStack matrixStack, String string, int x, int y, int color) {
        fontRenderer.draw(matrixStack, string, x, y, color);
    }
    
    public static void drawConfiguredStringOnHUD(
        PoseStack matrixStack, String string,
        int xOffset, int yOffset, int color, int relLineOffset
    ) {
        yOffset += (relLineOffset + StructuresCompassConfig.overlayLineOffset.get()) * 9;
        if (StructuresCompassConfig.hudPosition.get() == HUDPosition.LEFT)

            drawStringLeft(matrixStack, string, xOffset + StructuresCompassConfig.xOffset.get() - 5, yOffset + StructuresCompassConfig.yOffset.get() - 14, color);
        else
            drawStringRight(
                matrixStack, string,
                minecraft.getWindow().getGuiScaledWidth() - fontRenderer.width(string) - xOffset - StructuresCompassConfig.xOffset.get() + 5,
                yOffset + StructuresCompassConfig.yOffset.get() - 14, color
            );
    }
    
    public static void updateBuffer(@Nonnull BufferBuilder buffer, int startX, int startY, int endX, int endY) {
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
        buffer.vertex(startX, endY, 0.0D).endVertex();
        buffer.vertex(endX, endY, 0.0D).endVertex();
        buffer.vertex(endX, startY, 0.0D).endVertex();
        buffer.vertex(startX, startY, 0.0D).endVertex();
    }
    
    public static void drawRect(int left, int top, int right, int bottom, int color) {
        if (left < right)
            left = GeneralUtils.swap(right, right = left);
        if (top < bottom)
            top = GeneralUtils.swap(bottom, bottom = top);
        
        final float red = (float) (color >> 16 & 255) / 255.0F;
        final float green = (float) (color >> 8 & 255) / 255.0F;
        final float blue = (float) (color & 255) / 255.0F;
        final float alpha = (float) (color >> 24 & 255) / 255.0F;
        
        final Tesselator tesselator = Tesselator.getInstance();
        final BufferBuilder buffer = tesselator.getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.blendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO
        );
        
        RenderSystem.setShaderColor(red, green, blue, alpha);
        
        updateBuffer(buffer, left, top, right, bottom);
        tesselator.end();
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }
}
