package xyz.sunrose.wacky_wonders;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.random.RandomGenerator;

public class RandomHelper {
	public static Vec3d randomPolar (RandomGenerator random, double centerX, double y, double centerZ, double maxRadius, double yDev){
		float angle = random.nextFloat() * MathHelper.PI * 2;
		double r = MathHelper.clamp(random.nextGaussian(), -1, 1) * maxRadius;
		return new Vec3d(0, 0, r).rotateY(angle).add(centerX, y+random.nextDouble()*yDev, centerZ);
	}
}
