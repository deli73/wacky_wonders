package xyz.sunrose.wacky_wonders.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import xyz.sunrose.wacky_wonders.WackyWhimsicalWonders;
import xyz.sunrose.wacky_wonders.items.WackyItems;

import java.util.List;

public class SmokeBombEntity extends ThrownItemEntity {
	private static final int BLINDNESS_DURATION = 5*20; //max(base?) blindness duration?
	private static final double BLINDING_RADIUS = 5; //blinding range in blocks


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

			//get the entities in range, apply the blinding effect to them based roughly on proximity ("exposure")
			Vec3d corner = new Vec3d(BLINDING_RADIUS, BLINDING_RADIUS, BLINDING_RADIUS);
			Box box = new Box(corner.multiply(-1), corner).offset(this.getPos());
			List<Entity> hitEntities = world.getOtherEntities(null, box);
			for (Entity e : hitEntities) {
				applyBlinding(e, Explosion.getExposure(this.getPos(), e), this.squaredDistanceTo(e));
			}

			this.discard();
		}
	}

	private void applyBlinding(Entity entity, float exposure, double distanceSquared) {
		int duration_base = !(entity instanceof PlayerEntity) ? BLINDNESS_DURATION : BLINDNESS_DURATION / 2;
		double distance_factor = 1 - (distanceSquared / (BLINDING_RADIUS*BLINDING_RADIUS)); // basically 1-x² from x=0 to x=1
		int final_duration = (int) (duration_base * exposure * distance_factor);
		if (entity instanceof LivingEntity living) {
			StatusEffectInstance blindness = new StatusEffectInstance(StatusEffects.BLINDNESS, final_duration);
			WackyWhimsicalWonders.LOGGER.info("Applied blindness to "+entity.getType().toString()+" with duration "+final_duration);
			living.addStatusEffect(blindness, this);
		}
	}
}
