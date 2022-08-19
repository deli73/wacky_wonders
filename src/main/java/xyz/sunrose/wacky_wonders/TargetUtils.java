package xyz.sunrose.wacky_wonders;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class TargetUtils {
	// we don't know exactly how this works but it does, so fuck it
	public static @Nullable Entity getTarget(Entity from, double maxDistance){
		Vec3d eyePos = from.getEyePos();
		Vec3d rotation = from.getRotationVec(1.0F).multiply(maxDistance);
		Vec3d rotatedEyePos = eyePos.add(rotation);

		Box box = from.getBoundingBox().stretch(rotation).expand(1.0);
		double distSquared = maxDistance * maxDistance;

		Predicate<Entity> predicate = e -> !e.isSpectator() && e.collides();
		EntityHitResult hit = ProjectileUtil.raycast(from, eyePos, rotatedEyePos, box, predicate, distSquared);
		if (hit == null){return null;}

		WackyWhimsicalWonders.LOGGER.info("TEST");
		return eyePos.squaredDistanceTo(hit.getPos()) > distSquared ? null : hit.getEntity();
	}

	public static @Nullable BlockHitResult blockTarget(Entity from, double maxDistance){
		HitResult hit = from.raycast(maxDistance, 1f, false);
		if (hit.getType()== HitResult.Type.BLOCK){
			return (BlockHitResult) hit;
		}
		return null;
	}

	// yeet function
	public static void knockback(Entity attacker, LivingEntity target, double strength) {
		target.takeKnockback(
				strength,
				MathHelper.sin(attacker.getYaw() * (float) (Math.PI / 180.0)),
				-MathHelper.cos(attacker.getYaw() * (float) (Math.PI / 180.0))
		);
	}
}
