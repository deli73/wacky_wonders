package xyz.sunrose.wacky_wonders.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import xyz.sunrose.wacky_wonders.items.WackyItems;

public class FruitBombEntity extends ThrownItemEntity {
	public FruitBombEntity(EntityType<FruitBombEntity> entityType, World world) {super(entityType, world);}

	public FruitBombEntity(World world, LivingEntity livingEntity) {
		super(WackyEntities.FRUIT_BOMB_ENTITY_TYPE, livingEntity, world);
	}

	@Override
	protected Item getDefaultItem() {
		return WackyItems.FRUIT_BOMB;
	}

	@Override
	protected void onCollision(HitResult hitResult) {
		super.onCollision(hitResult);
		if (!this.world.isClient && !this.isRemoved()) {
			this.world.createExplosion(this, this.getX(), this.getBodyY(0.0625), this.getZ(), 2f, Explosion.DestructionType.NONE);
			this.discard();
		}
	}
}
