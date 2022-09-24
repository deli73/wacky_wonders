package xyz.sunrose.wacky_wonders.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import juuxel.libninepatch.ContextualTextureRenderer;
import juuxel.libninepatch.NinePatch;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import xyz.sunrose.wacky_wonders.WWW;
import xyz.sunrose.wacky_wonders.blocks.WBlocks;
import xyz.sunrose.wacky_wonders.entities.WEntities;

public class WWWClient implements ClientModInitializer {
	public static final Identifier GLASES_OVERLAY_ID = WWW.id("textures/gui/glasses_overlay.png");
	public static final NinePatch<Identifier> GLASSES_OVERLAY = NinePatch.builder(WWWClient.GLASES_OVERLAY_ID)
			.cornerUv(5/64f).cornerSize(10).mode(NinePatch.Mode.STRETCHING).build();

	public static final float GLASSES_GLOWING_RANGE = 16;
	public static final float GLASSES_GLOWING_RANGE_NV = 32;

	@Override
	public void onInitializeClient(ModContainer mod) {
		// == BLOCK RENDERERS ==
		BlockEntityRendererRegistry.register(WBlocks.MACHINING_TABLE_ENTITY_TYPE, MachiningTableRenderer::new);

		// == ENTITY RENDERERS ==
		EntityRendererRegistry.register(
				WEntities.FRUIT_BOMB_ENTITY_TYPE, GlowingFlyingItemEntityRenderer::new
		);
		EntityRendererRegistry.register(
				WEntities.SMOKE_BOMB_ENTITY_TYPE, FlyingItemEntityRenderer::new
		);
	}

	public enum RendererImpl implements ContextualTextureRenderer<Identifier, MatrixStack> {
		INSTANCE;
		@Override
		public void draw(Identifier texture, MatrixStack matrices, int x, int y, int width, int height, float u1, float v1, float u2, float v2) {
			texturedRect(matrices, x, y, width, height, texture, u1, v1, u2, v2, 0xFF_FFFFFF, 1f);
		}
	}

	public static void texturedRect(MatrixStack matrices, int x, int y, int width, int height, Identifier texture, float u1, float v1, float u2, float v2, int color, float opacity) {
		if (width <= 0) width = 1;
		if (height <= 0) height = 1;

		float r = (color >> 16 & 255) / 255.0F;
		float g = (color >> 8 & 255) / 255.0F;
		float b = (color & 255) / 255.0F;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBufferBuilder();
		Matrix4f model = matrices.peek().getPosition();
		RenderSystem.enableBlend();
		RenderSystem.setShaderTexture(0, texture);
		RenderSystem.setShaderColor(r,g,b, opacity);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		buffer.vertex(model, x,y + height, -90).uv(u1, v2).next();
		buffer.vertex(model,x + width, y + height, -90).uv(u2, v2).next();
		buffer.vertex(model,x + width, y,-90).uv(u2, v1).next();
		buffer.vertex(model,x,y,-90).uv(u1, v1).next();
		BufferRenderer.drawWithShader(buffer.end());
		RenderSystem.disableBlend();
	}
}
