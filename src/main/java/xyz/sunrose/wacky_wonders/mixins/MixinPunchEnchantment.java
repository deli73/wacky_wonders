package xyz.sunrose.wacky_wonders.mixins;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.PunchEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import xyz.sunrose.wacky_wonders.items.WItems;

@Mixin(PunchEnchantment.class)
public class MixinPunchEnchantment extends Enchantment {
	protected MixinPunchEnchantment(Rarity rarity, EnchantmentTarget enchantmentTarget, EquipmentSlot[] equipmentSlots) {
		super(rarity, enchantmentTarget, equipmentSlots);
	}

	public boolean isAcceptableItem(ItemStack stack) {
		return this.type.isAcceptableItem(stack.getItem()) || stack.getItem() == WItems.SPRING_BOXER;
	}
}
