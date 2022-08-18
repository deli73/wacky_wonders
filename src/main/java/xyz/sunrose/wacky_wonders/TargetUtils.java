package xyz.sunrose.wacky_wonders;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class TargetUtils {
	// we don't know exactly how this works but it probably does??
	public static @Nullable Entity getTarget(Entity entity, double maxDistance){
		Vec3d eyePos = entity.getEyePos();
		Vec3d rotation = entity.getRotationVec(1.0F).multiply(maxDistance);
		Vec3d rotatedEyePos = eyePos.add(rotation);

		Box box = entity.getBoundingBox().stretch(rotation).expand(1.0);
		double distSquared = maxDistance * maxDistance;

		Predicate<Entity> predicate = e -> !e.isSpectator() && e.collides();
		EntityHitResult hit = ProjectileUtil.raycast(entity, eyePos, rotatedEyePos, box, predicate, distSquared);
		if (hit == null){return null;}

		return eyePos.squaredDistanceTo(hit.getPos()) > distSquared ? null : hit.getEntity();
	}

	public static void knockback(Entity attacker, LivingEntity target, int strength) {
		target.takeKnockback(
				strength,
				MathHelper.sin(attacker.getYaw() * (float) (Math.PI / 180.0)),
				-MathHelper.cos(attacker.getYaw() * (float) (Math.PI / 180.0))
		);
	}
}
