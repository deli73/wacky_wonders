package xyz.sunrose.wacky_wonders.mixins.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.sunrose.wacky_wonders.client.WWWClient;
import xyz.sunrose.wacky_wonders.items.WItems;

@Mixin(InGameHud.class)
public class MixinInGameHud {
	@Shadow
	@Final
	private MinecraftClient client;

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getFrozenTicks()I"))
	private void onOverlayRenders(MatrixStack matrices, float tickDelta, CallbackInfo ci){
		if (client.player != null) {
			ItemStack headSlot = client.player.getInventory().getArmorStack(PlayerInventory.HELMET_SLOTS[0]);
			if (headSlot.getItem() == WItems.GLASSES) {
				int scaledWidth = client.getWindow().getScaledWidth();
				int scaledHeight = client.getWindow().getScaledHeight();

				WWWClient.GLASSES_OVERLAY.draw(WWWClient.RendererImpl.INSTANCE, matrices, scaledWidth, scaledHeight);
			}
		}
	}

}
