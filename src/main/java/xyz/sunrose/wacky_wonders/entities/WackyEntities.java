package xyz.sunrose.wacky_wonders.entities;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import xyz.sunrose.wacky_wonders.WackyWhimsicalWonders;

public class WackyEntities {
	public static final EntityType<FruitBombEntity> FRUIT_BOMB_ENTITY_TYPE = Registry.register(
			Registry.ENTITY_TYPE, new Identifier(WackyWhimsicalWonders.MODID, "fruit_bomb"),
			FabricEntityTypeBuilder.<FruitBombEntity>create(SpawnGroup.MISC, FruitBombEntity::new)
					.dimensions(EntityDimensions.fixed(0.25f, 0.25f))
					.trackRangeBlocks(4)
					.trackedUpdateRate(10)
					.build()
	);

	public static final EntityType<SmokeBombEntity> SMOKE_BOMB_ENTITY_TYPE = Registry.register(
			Registry.ENTITY_TYPE, new Identifier(WackyWhimsicalWonders.MODID, "smoke_bomb"),
			FabricEntityTypeBuilder.<SmokeBombEntity>create(SpawnGroup.MISC, SmokeBombEntity::new)
					.dimensions(EntityDimensions.fixed(0.25f, 0.25f))
					.trackRangeBlocks(4)
					.trackedUpdateRate(10)
					.build()
	);

	public static void init() {

	}
}
