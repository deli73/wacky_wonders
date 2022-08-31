package xyz.sunrose.wacky_wonders.items;

import com.unascribed.lib39.recoil.api.DirectClickItem;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Vanishable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import xyz.sunrose.wacky_wonders.util.TargetUtils;

public class SpringBoxerItem extends Item implements Vanishable, DirectClickItem {
	private static final String CHARGED_KEY = "Charged";
	private static final int MAX_CHARGE_DURATION = 25;
	private static final float CHARGE_PERCENT_START_CHARGING_SOUND = 0.2f;
	private static final float CHARGE_PERCENT_MID_CHARGE_SOUND = 0.5f;
	private static final int KNOCKBACK_STRENGTH = 3;

	private static final double SELF_LAUNCH_RANGE = 3;
	private static final double SELF_LAUNCH_STRENGTH = 1;
	private static final double SELF_LAUNCH_PUNCH_MULTIPLIER = 1d/6;
	// vertical self-launch heights:
	// PUNCH 0 -> ~5      blocks
	// PUNCH 1 -> ~6 +7/8 blocks
	// PUNCH 2 -> ~8 +6/8 blocks

	private boolean charged = false;
	private boolean loaded = false;

	public SpringBoxerItem(Settings settings) {
		super(settings);
	}

	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		if(isCharged(stack) && fire(world, user, hand, stack)){
			setCharged(stack, false);
			return TypedActionResult.consume(stack);
		} else {
			if (!isCharged(stack)) {
				this.charged = false;
				this.loaded = false;
				user.setCurrentHand(hand);
			}

			return TypedActionResult.fail(stack);
		}
	}

	private static boolean fire(World world, PlayerEntity user, Hand hand, ItemStack stack) {
		// do knockback if applicable
		BlockPos soundSpot = user.getBlockPos();
		int punchLevel = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
		@Nullable Entity target = TargetUtils.getTarget(user, 4.5);
		@Nullable BlockHitResult blockHit = TargetUtils.blockTarget(user, SELF_LAUNCH_RANGE);
		if (target instanceof LivingEntity entity) {
			TargetUtils.knockback(user, entity,
					KNOCKBACK_STRENGTH + punchLevel
			);
			soundSpot = entity.getBlockPos();
		} else if (target == null && user.isOnGround() && blockHit != null) {
			BlockState state = world.getBlockState(blockHit.getBlockPos());
			if (!state.getCollisionShape( world, blockHit.getBlockPos() ).isEmpty()) {
				Vec3d rotation = user.getRotationVec(1F).normalize();
				Vec3d launch = rotation.multiply(SELF_LAUNCH_STRENGTH + punchLevel * SELF_LAUNCH_PUNCH_MULTIPLIER);
				user.addVelocity(-launch.x, -launch.y, -launch.z);
			} else {
				return false;
			}
		} else {
			return false;
		}

		// update stats
		if (user instanceof ServerPlayerEntity player) {
			player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
		}

		// damage item
		stack.damage(1, user, e -> e.sendToolBreakStatus(hand));

		// play sound
		world.playSound(user, soundSpot, SoundEvents.ITEM_CROSSBOW_SHOOT, SoundCategory.PLAYERS,
				1.0F, 1.0F / (world.random.nextFloat() * 0.5F + 1.8F)+0.7F
		);
		return true;
	}

	@Override
	public ActionResult onDirectAttack(PlayerEntity player, Hand hand) {
		return ActionResult.PASS;
	}

	@Override
	public ActionResult onDirectUse(PlayerEntity player, Hand hand) {
		Entity hit = TargetUtils.getTarget(player, 4.5);
		if (hit instanceof ArmorStandEntity armorStand && isCharged(player.getStackInHand(hand))) {
			if (player.world.isClient && !armorStand.hasNoGravity()){
				player.playSound(SoundEvents.ITEM_CROSSBOW_SHOOT, SoundCategory.PLAYERS,
						1.0F, 1.0F / (player.world.random.nextFloat() * 0.5F + 1.8F)+0.7F);
				return ActionResult.SUCCESS;
			}
			return onPlayerUse(player, player.getWorld(), hand, hit);
		}
		return ActionResult.PASS;
	}

	// == CALLBACK FOR ARMORSTAND OVERRIDE ==
	public static ActionResult onPlayerUse(PlayerEntity player, World world, Hand hand, Entity entity) {

		if(entity instanceof ArmorStandEntity armorStand && !armorStand.hasNoGravity()) {
			ItemStack stack = player.getStackInHand(hand);
			if (stack.getItem() == WackyItems.SPRING_BOXER) {
				if (player.isSpectator()) {return ActionResult.PASS;}
				if(isCharged(stack) && fire(world, player, hand, stack)){
					setCharged(stack, false);
					return TypedActionResult.consume(stack).getResult();
				} else {
					if (!isCharged(stack)) {
						player.setCurrentHand(hand);
					}

					return TypedActionResult.fail(stack).getResult();
				}
			}
		}
		return ActionResult.PASS;
	}

	// == ENCHANTMENT ==


	@Override
	public int getEnchantability() {
		return 11;
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
