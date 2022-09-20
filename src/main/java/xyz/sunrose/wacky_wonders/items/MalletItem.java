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
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Vanishable;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.quiltmc.loader.api.QuiltLoader;
import xyz.sunrose.wacky_wonders.compat.IntegrationPehkui;
import xyz.sunrose.wacky_wonders.events.WSounds;

public class MalletItem extends Item implements Vanishable {
	private static final int SLOW_TIME = 40;
	private static final int SLOW_LEVEL = 2;
	private static final int NEG_JUMP_BOOST = 127;
	private static final double ATTACK_SPEED = -1.5;

	private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

	public MalletItem(Settings settings) {
		super(settings);

		ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> attributeBuilder = ImmutableMultimap.builder();
		attributeBuilder.put(
				EntityAttributes.GENERIC_ATTACK_SPEED,
				new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon Modifier", ATTACK_SPEED, EntityAttributeModifier.Operation.ADDITION)
		);
		this.attributeModifiers = attributeBuilder.build();
	}

	@Override
	public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
		return false;
	}

	public static ActionResult onPlayerAttack(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
		if (!player.isSpectator() && player.getStackInHand(hand).getItem() == WItems.MALLET && entity instanceof LivingEntity target) {
			if (world.isClient) {return ActionResult.SUCCESS;}
			if (QuiltLoader.isModLoaded("pehkui")){
				IntegrationPehkui.malletAttack(player, world, target);
			}
			target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, SLOW_TIME, SLOW_LEVEL-1));
			target.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, SLOW_TIME, NEG_JUMP_BOOST));
			target.playSound(WSounds.SOUND_BONK, 1f, 1f);
			player.resetLastAttackedTicks();
			return ActionResult.SUCCESS;
		}

		return ActionResult.PASS;
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? attributeModifiers : super.getAttributeModifiers(slot);
	}
}
