package xyz.sunrose.wacky_wonders.compat;

import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleEasings;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleTypes;
import xyz.sunrose.wacky_wonders.WWW;

public class IntegrationPehkui {
	private static final int UNSQUISH_TIME = 40;
	private static final float SQUISH_FACTOR = 0.5f;
	/*private static final Float2FloatFunction SQUISH_EASE_BEZIER = ScaleRegistries.register(ScaleRegistries.SCALE_EASINGS, WWW.id("snap_bezier"),
			CubicBezier.cubic_bezier(1,-0.19,.68,1.41)
	);*/
	private static final Float2FloatFunction SQUISH_EASE_ELASTIC = ScaleRegistries.register(ScaleRegistries.SCALE_EASINGS, WWW.id("snap_elastic"),
			x -> {
				if (x < 0.5F)
				{
					return ScaleEasings.EXPONENTIAL_IN_OUT.get(x);
				} else {
					return ScaleEasings.ELASTIC_IN_OUT.get(x);
				}
			}
	);

	public static void malletAttack(PlayerEntity player, World world, LivingEntity target) {
		ScaleData data = ScaleTypes.MODEL_HEIGHT.getScaleData(target);
		float currentTarget = data.getTargetScale();
		data.setScale(currentTarget * SQUISH_FACTOR);
		data.setTargetScale(currentTarget);
		data.setScaleTickDelay(UNSQUISH_TIME);
		data.setEasing(
				SQUISH_EASE_ELASTIC
		);
	}
}
