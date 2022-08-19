package xyz.sunrose.wacky_wonders.mixins;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.QuickChargeEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import xyz.sunrose.wacky_wonders.items.WackyItems;

@Mixin(QuickChargeEnchantment.class)
public class MixinQuickChargeEnchantment extends Enchantment {
	protected MixinQuickChargeEnchantment(Rarity rarity, EnchantmentTarget enchantmentTarget, EquipmentSlot[] equipmentSlots) {
		super(rarity, enchantmentTarget, equipmentSlots);
	}

	public boolean isAcceptableItem(ItemStack stack) {
		return this.type.isAcceptableItem(stack.getItem()) || stack.getItem() == WackyItems.SPRING_BOXER;
	}
}
