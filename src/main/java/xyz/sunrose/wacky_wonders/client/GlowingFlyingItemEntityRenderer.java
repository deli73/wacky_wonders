package xyz.sunrose.wacky_wonders.client;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FlyingItemEntity;

public class GlowingFlyingItemEntityRenderer<T extends Entity & FlyingItemEntity> extends FlyingItemEntityRenderer<T>{
	public GlowingFlyingItemEntityRenderer(EntityRendererFactory.Context context, float f, boolean bl) {
		super(context, f, bl);
	}

	public GlowingFlyingItemEntityRenderer(EntityRendererFactory.Context context) {
		this(context, 1.0F, true);
	}
}
