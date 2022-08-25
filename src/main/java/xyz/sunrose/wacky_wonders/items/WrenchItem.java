package xyz.sunrose.wacky_wonders.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Vanishable;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import xyz.sunrose.wacky_wonders.api.WrenchBoostable;
import xyz.sunrose.wacky_wonders.mixins.AccessorAbstractFurnaceBlockEntity;

public class WrenchItem extends Item implements Vanishable {
	protected static final int TICKS_ADDED = 25;
	private static final double ATTACK_SPEED = -1.8f;

	private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

	public WrenchItem(Settings settings) {
		super(settings);

		ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> attributeBuilder = ImmutableMultimap.builder();
		attributeBuilder.put(
				EntityAttributes.GENERIC_ATTACK_SPEED,
				new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon Modifier", ATTACK_SPEED, EntityAttributeModifier.Operation.ADDITION)
		);
		this.attributeModifiers = attributeBuilder.build();
	}

	public static ActionResult onAttackBlock(PlayerEntity playerEntity, World world, Hand hand, BlockPos blockPos, Direction _direction) {
		if (hand != Hand.MAIN_HAND || playerEntity.getStackInHand(hand).getItem() != WackyItems.WRENCH || playerEntity.getAttackCooldownProgress(0f) < 1) {
			// only works if the player has a wrench in their main hand and the attack cooldown is over
			return ActionResult.PASS;
		}

		// SPEEDUP
		BlockEntity blockEntity = world.getBlockEntity(blockPos);
		if (blockEntity instanceof AbstractFurnaceBlockEntity furnace) { //boost vanilla furnace type blocks
			AccessorAbstractFurnaceBlockEntity access = (AccessorAbstractFurnaceBlockEntity) furnace;
			int currentBurnTime = access.getBurnTime();
			int currentCookTime = access.getCookTime();
			int maxCookTime = access.getCookTimeTotal() - 1;

			int diff = Math.min(maxCookTime - currentCookTime, TICKS_ADDED);
			if(currentBurnTime > diff) {
				access.setBurnTime(currentBurnTime - diff);
				access.setCookTime(currentCookTime + diff);
				furnace.markDirty();
				world.playSound(playerEntity, blockPos, SoundEvents.BLOCK_SMITHING_TABLE_USE, SoundCategory.PLAYERS, 1f, 1.2f);
				playerEntity.resetLastAttackedTicks();
				return ActionResult.SUCCESS;
			}
		} else if (blockEntity instanceof WrenchBoostable boostable) { //boost any blocks that support the wrench boosting API
			return boostable.accelerate(TICKS_ADDED) ? ActionResult.SUCCESS : ActionResult.PASS;
		}
		return ActionResult.PASS;
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		BlockPos pos = context.getBlockPos();
		BlockState state = context.getWorld().getBlockState(pos);
		Vec3d hitPos = context.getHitPos();
		PlayerEntity player = context.getPlayer();


		return ActionResult.PASS;
	}

	@Override
	public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
		return false;
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? attributeModifiers : super.getAttributeModifiers(slot);
	}
}
