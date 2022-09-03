package xyz.sunrose.wacky_wonders.items;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class SparkeliteBottleItem extends Item {
	private static final StatusEffectInstance[] effects = {
			new StatusEffectInstance(StatusEffects.LEVITATION, 100, 1, true, true, true),
			new StatusEffectInstance(StatusEffects.JUMP_BOOST, 500, 1, true, true, true),
			new StatusEffectInstance(StatusEffects.INVISIBILITY, 200, 0, true, true, true),
			new StatusEffectInstance(StatusEffects.SLOW_FALLING, 200, 0, true, true, true),
			new StatusEffectInstance(StatusEffects.GLOWING, 500, 0, true, true, true),
			new StatusEffectInstance(StatusEffects.SPEED, 500, 1, true, true, true)
	};

	public SparkeliteBottleItem(Settings settings) {
		super(settings);
	}

	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		PlayerEntity playerEntity = user instanceof PlayerEntity ? (PlayerEntity)user : null;

		if (playerEntity instanceof ServerPlayerEntity) {
			Criteria.CONSUME_ITEM.trigger((ServerPlayerEntity)playerEntity, stack);
		}

		if (!world.isClient) {
			// magic time
			int effectIndex1 = world.random.nextInt(effects.length);
			int effectIndex2 = world.random.nextInt(effects.length);
			user.addStatusEffect(new StatusEffectInstance(effects[effectIndex1])); //note to self: don't reuse status effect instances!!
			if(effectIndex1 != effectIndex2) {
				user.addStatusEffect(new StatusEffectInstance(effects[effectIndex2]));
			}
		}

		// all copied from PotionItem lol
		if (playerEntity != null) {
			playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
			if (!playerEntity.getAbilities().creativeMode) {
				stack.decrement(1);
			}
		}

		if (playerEntity == null || !playerEntity.getAbilities().creativeMode) {
			if (stack.isEmpty()) {
				return new ItemStack(Items.GLASS_BOTTLE);
			}

			if (playerEntity != null) {
				playerEntity.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE));
			}
		}

		world.emitGameEvent(user, GameEvent.DRINK, user.getCameraPosVec(1f));
		return stack;
	}

	public int getMaxUseTime(ItemStack stack) {
		return 32;
	}

	public UseAction getUseAction(ItemStack stack) {
		return UseAction.DRINK;
	}

	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		return ItemUsage.consumeHeldItem(world, user, hand);
	}
}
