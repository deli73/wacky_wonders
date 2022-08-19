package xyz.sunrose.wacky_wonders.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import xyz.sunrose.wacky_wonders.TargetUtils;

import java.util.UUID;

public class BoxingGloveItem extends Item {
	//generated with a fair website, guarunteed to be random
	private static final UUID GLOVE_KNOCKBACK_RESISTANCE_UUID = UUID.fromString("B56F9362-1FA4-11ED-861D-0242AC120002");
	private static final UUID GLOVE_KNOCKBACK_UUID = UUID.fromString("9D2CDD92-1FAD-11ED-861D-0242AC120002");

	private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

	protected static final float ATTACK_SPEED = -1.5f;

	public BoxingGloveItem(Settings settings) {
		super(settings);
		// boxing gloves set your attack damage to 0, increase your knockback resistance, and set your attack speed to the one above
		ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(
				EntityAttributes.GENERIC_ATTACK_DAMAGE,
				new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon Modifier", -1, EntityAttributeModifier.Operation.ADDITION)
		);
		builder.put(
				EntityAttributes.GENERIC_ATTACK_SPEED,
				new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon Modifier", ATTACK_SPEED, EntityAttributeModifier.Operation.ADDITION)
		);
		builder.put(
				EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE,
				new EntityAttributeModifier(GLOVE_KNOCKBACK_RESISTANCE_UUID, "Glove Modifier", 0.1f, EntityAttributeModifier.Operation.ADDITION)
		);
		// NOTE: ACTUAL KNOCKBACK OCCURS ELSEWHERE
		builder.put(
				EntityAttributes.GENERIC_ATTACK_KNOCKBACK,
				new EntityAttributeModifier(GLOVE_KNOCKBACK_UUID, "Weapon Modifier", 2, EntityAttributeModifier.Operation.ADDITION)
		);
		this.attributeModifiers = builder.build();
	}


	public static ActionResult onPlayerAttack(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
		// apply knockback if applicable
		if(player.getStackInHand(hand).getItem() == WackyItems.BOXING_GLOVE && entity instanceof LivingEntity target){
			TargetUtils.knockback(player, target, 1);
			world.playSound(
					null, player.getX(), player.getY(), player.getZ(),
					SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, player.getSoundCategory(), 1f, 1f
			);
			return ActionResult.SUCCESS;
		}
		return ActionResult.PASS;
	}

	@Override
	public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
		// boxing gloves also prevent you from mining
		return false;
	}


	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot);
	}
}
