package xyz.sunrose.wacky_wonders.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import xyz.sunrose.wacky_wonders.WackyWhimsicalWonders;

public class WackyEntities {
	public static final EntityType<FruitBombEntity> FRUIT_BOMB_ENTITY_TYPE = Registry.register(
			Registry.ENTITY_TYPE, new Identifier(WackyWhimsicalWonders.MODID, "fruit_bomb"),
			EntityType.Builder.<FruitBombEntity>create(FruitBombEntity::new, SpawnGroup.MISC)
					.setDimensions(0.25F, 0.25F)
					.maxTrackingRange(4)
					.trackingTickInterval(10)
					.build("fruit_bomb")
	);

	public static void init() {

	}
}
