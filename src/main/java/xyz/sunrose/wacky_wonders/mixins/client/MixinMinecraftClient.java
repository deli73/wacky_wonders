package xyz.sunrose.wacky_wonders.mixins.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerInventory;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.sunrose.wacky_wonders.client.WWWClient;
import xyz.sunrose.wacky_wonders.items.WItems;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {
	@Shadow
	@Nullable
	public ClientPlayerEntity player;

	@Inject(method = "hasOutline", at = @At("RETURN"), cancellable = true)
	private void injectOutline(Entity entity, CallbackInfoReturnable<Boolean> cir) {
		if(this.player != null) {
			// range longer if player has night vision
			float range = this.player.hasStatusEffect(StatusEffects.NIGHT_VISION) ? WWWClient.GLASSES_GLOWING_RANGE_NV : WWWClient.GLASSES_GLOWING_RANGE;
			if (this.player.getInventory().getArmorStack(PlayerInventory.HELMET_SLOTS[0]).getItem() == WItems.GLASSES
					&& entity instanceof MobEntity && entity.distanceTo(this.player) < range
			) { //if the player has the glasses on, and the entity is a mob that's within range, it glows
				cir.setReturnValue(true);
			}
		}
	}
}
