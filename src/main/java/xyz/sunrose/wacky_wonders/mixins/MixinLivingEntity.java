package xyz.sunrose.wacky_wonders.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {
	static double BLINDNESS_FACTOR = 0.1; // percentage of attack range a mob retains when blind

	@Inject(method = "getAttackDistanceScalingFactor", at=@At(value = "TAIL"))
	private void injectAttackDistance(Entity entity, CallbackInfoReturnable<Double> cir){
		if(entity instanceof LivingEntity living) {
			Map<StatusEffect, StatusEffectInstance> effects = living.getActiveStatusEffects();
			if (effects.containsKey(StatusEffects.BLINDNESS)){
				cir.setReturnValue( cir.getReturnValue() * BLINDNESS_FACTOR);
			}
		}
	}
}
