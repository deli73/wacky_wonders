package xyz.sunrose.wacky_wonders.items;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import xyz.sunrose.wacky_wonders.TargetUtils;

public class SpringBoxerItem extends Item {
	private static final String CHARGED_KEY = "Charged";
	private static final int MAX_CHARGE_DURATION = 25;
	private static final float CHARGE_PERCENT_START_CHARGING_SOUND = 0.2F;
	private static final float CHARGE_PERCENT_MID_CHARGE_SOUND = 0.5F;
	private static final int KNOCKBACK_STRENGTH = 3;
	private boolean charged = false;
	private boolean loaded = false;

	public SpringBoxerItem(Settings settings) {
		super(settings);
	}

	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		if(isCharged(stack)){
			fire(world, user, hand, stack);
			setCharged(stack, false);
			return TypedActionResult.consume(stack);
		} else {
			if (!isCharged(stack)) {
				this.charged = false;
				this.loaded = false;
				user.setCurrentHand(hand);
			}

			return TypedActionResult.consume(stack);
		}
	}



	private void fire(World world, PlayerEntity user, Hand hand, ItemStack stack) {
		// do knockback if applicable
		@Nullable Entity target = TargetUtils.getTarget(user, 4.5);
		if (target instanceof LivingEntity entity) {
			TargetUtils.knockback(user, entity, KNOCKBACK_STRENGTH);
			world.playSound(user, entity.getBlockPos(), SoundEvents.ITEM_CROSSBOW_SHOOT, SoundCategory.PLAYERS,
					1.0F, 1.0F / (world.random.nextFloat() * 0.5F + 1.8F)+0.7F
			);
		}

		// update stats
		if (user instanceof ServerPlayerEntity player) {
			player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
		}
	}

	// == FROM CROSSBOW CODE ==
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		int i = this.getMaxUseTime(stack) - remainingUseTicks;
		float pullProgress = getPullProgress(i, stack);
		if (pullProgress >= 1.0F && !isCharged(stack)) {
			setCharged(stack, true);
			SoundCategory soundCategory = user instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.HOSTILE;
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_CROSSBOW_LOADING_END,
					soundCategory, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F
			);
		}
	}

	public static boolean isCharged(ItemStack stack) {
		NbtCompound nbtCompound = stack.getNbt();
		return nbtCompound != null && nbtCompound.getBoolean(CHARGED_KEY);
	}

	public static void setCharged(ItemStack stack, boolean charged) {
		NbtCompound nbtCompound = stack.getOrCreateNbt();
		nbtCompound.putBoolean(CHARGED_KEY, charged);
	}

	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		if (!world.isClient) {
			int quick_charge_level = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
			SoundEvent quickChargeSound = this.getQuickChargeSound(quick_charge_level);
			SoundEvent loadingSound = quick_charge_level == 0 ? SoundEvents.ITEM_CROSSBOW_LOADING_MIDDLE : null;
			float charge_percent = (float)(stack.getMaxUseTime() - remainingUseTicks) / (float)getPullTime(stack);
			if (charge_percent < CHARGE_PERCENT_START_CHARGING_SOUND) {
				this.charged = false;
				this.loaded = false;
			}

			if (charge_percent >= CHARGE_PERCENT_START_CHARGING_SOUND && !this.charged) {
				this.charged = true;
				world.playSound(null, user.getX(), user.getY(), user.getZ(), quickChargeSound, SoundCategory.PLAYERS, 0.5F, 1.0F);
			}

			if (charge_percent >= CHARGE_PERCENT_MID_CHARGE_SOUND && loadingSound != null && !this.loaded) {
				this.loaded = true;
				world.playSound(null, user.getX(), user.getY(), user.getZ(), loadingSound, SoundCategory.PLAYERS, 0.5F, 1.0F);
			}
		}
	}

	public int getMaxUseTime(ItemStack stack) {
		return getPullTime(stack) + 3;
	}

	public static int getPullTime(ItemStack stack) {
		int i = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
		return i == 0 ? MAX_CHARGE_DURATION : MAX_CHARGE_DURATION - (MAX_CHARGE_DURATION/5) * i;
	}

	public UseAction getUseAction(ItemStack stack) {
		return UseAction.CROSSBOW;
	}

	private SoundEvent getQuickChargeSound(int stage) {
		return switch (stage) {
			case 1 -> SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_1;
			case 2 -> SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_2;
			case 3 -> SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_3;
			default -> SoundEvents.ITEM_CROSSBOW_LOADING_START;
		};
	}

	private static float getPullProgress(int useTicks, ItemStack stack) {
		float progress = (float)useTicks / (float)getPullTime(stack);
		return Math.min(progress, 1F);
	}

	public boolean isUsedOnRelease(ItemStack stack) {
		return stack.isOf(this);
	}

}
