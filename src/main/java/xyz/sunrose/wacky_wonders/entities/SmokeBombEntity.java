package xyz.sunrose.wacky_wonders.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import xyz.sunrose.wacky_wonders.RandomHelper;
import xyz.sunrose.wacky_wonders.items.WackyItems;

import java.util.List;

public class SmokeBombEntity extends ThrownItemEntity {
	private static final int PARTICLE_COUNT = 20;
	private static final int BLINDNESS_DURATION = 6*20; //max(base?) blindness duration?
	private static final float DIRECT_HIT_MULTIPLIER = 2; //factor for increased time if entity is hit directly
	private static final double RADIUS = 5; //blinding and particle range in blocks
	private static final double PARTICLE_VELOCITY_MULTIPLIER = 0.01;


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
		spawnSmoke();

		if (!this.world.isClient && !this.isRemoved()) {
			if (hitResult.getType() == HitResult.Type.ENTITY){ //ensure more powerful hit if entity hit directly
				EntityHitResult entityHitResult = (EntityHitResult) hitResult;
				Entity entity = entityHitResult.getEntity();
				applyBlinding(entity, DIRECT_HIT_MULTIPLIER, 0);
			}
			//get the entities in range, apply the blinding effect to them based roughly on proximity ("exposure")
			Vec3d corner = new Vec3d(RADIUS, RADIUS, RADIUS);
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
		double distance_factor = 1 - (distanceSquared / (RADIUS * RADIUS)); // basically 1-x² from x=0 to x=1
		int final_duration = (int) (duration_base * exposure * distance_factor);
		if (entity instanceof LivingEntity living && !entity.equals(this.getOwner())) {
			StatusEffectInstance blindness = new StatusEffectInstance(StatusEffects.BLINDNESS, final_duration);
			//WackyWhimsicalWonders.LOGGER.info("Applied blindness to "+entity.getType().toString()+" with duration "+final_duration);
			living.addStatusEffect(blindness, this);
		}
	}

	private void spawnSmoke() { // fixme: client side collision code failing Sometimes™... might be a vanilla issue thx mojang
		RandomGenerator random = world.getRandom();
		//WackyWhimsicalWonders.LOGGER.info("spawnSmoke run on "+(world.isClient ? "CLIENT" : "SERVER"));
		for (int i = 0; i < PARTICLE_COUNT; i++) {
			Vec3d pos = RandomHelper.randomPolar(random, this.getX(), this.getY(), this.getZ(), RADIUS, 1);
			double xSpeed = (random.nextDouble() - 0.5) * 2;
			double zSpeed = (random.nextDouble() - 0.5) * 2;
			world.addImportantParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, random.nextBoolean(), pos.x, pos.y, pos.z,
					PARTICLE_VELOCITY_MULTIPLIER * xSpeed, 0, PARTICLE_VELOCITY_MULTIPLIER * zSpeed
			);
		}
	}
}
