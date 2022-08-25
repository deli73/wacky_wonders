package xyz.sunrose.wacky_wonders.client;

import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import xyz.sunrose.wacky_wonders.blocks.WackyBlocks;
import xyz.sunrose.wacky_wonders.entities.WackyEntities;

public class WackyWhimsicalWondersClient implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer mod) {
		// == BLOCK RENDERERS ==
		BlockEntityRendererRegistry.register(WackyBlocks.MACHINING_TABLE_ENTITY_TYPE, MachiningTableRenderer::new);

		// == ENTITY RENDERERS ==
		EntityRendererRegistry.register(
				WackyEntities.FRUIT_BOMB_ENTITY_TYPE, GlowingFlyingItemEntityRenderer::new
		);
		EntityRendererRegistry.register(
				WackyEntities.SMOKE_BOMB_ENTITY_TYPE, FlyingItemEntityRenderer::new
		);
	}
}
