package xyz.sunrose.wacky_wonders.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import xyz.sunrose.wacky_wonders.items.WackyItems;

public class SmokeBombEntity extends ThrownItemEntity {
	private static final int BLINDNESS_DURATION = 5*20;
	private static final int BLINDNESS_DURATION_POINTBLANK = 10*20;
	private static final double MAX_RANGE = 5; //blinding range in blocks


	public SmokeBombEntity(EntityType<SmokeBombEntity> smokeBombEntityEntityType, World world) {
		super(smokeBombEntityEntityType, world);
	}

	public SmokeBombEntity(World world, LivingEntity livingEntity) {
		super(WackyEntities.SMOKE_BOMB_ENTITY_TYPE, livingEntity, world);
	}

	@Override
	protected Item getDefaultItem() {return WackyItems.SMOKE_BOMB;}

	@Override
	protected void onCollision(HitResult hitResult) {
		super.onCollision(hitResult);
		if (!this.world.isClient && !this.isRemoved()) {
			// TODO add smoke explosion
			this.discard();
		}
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		if (entityHitResult.getEntity() instanceof LivingEntity living) {
			// TODO exclude thrower
			StatusEffectInstance blindness = new StatusEffectInstance(StatusEffects.BLINDNESS, BLINDNESS_DURATION_POINTBLANK);
			living.addStatusEffect(blindness, this);
		}
	}
}
