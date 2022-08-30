package xyz.sunrose.wacky_wonders.items;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.Util;
import net.minecraft.util.math.Position;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;
import xyz.sunrose.wacky_wonders.WackyWhimsicalWonders;
import xyz.sunrose.wacky_wonders.entities.FruitBombEntity;
import xyz.sunrose.wacky_wonders.entities.SmokeBombEntity;

public class WackyItems {
	public static final Item SPRING = Registry.register(
			Registry.ITEM, new Identifier(WackyWhimsicalWonders.MODID, "spring"),
			new Item(new QuiltItemSettings()
					.group(ItemGroup.MATERIALS)
			)
	);

	public static final Item BOXING_GLOVE = Registry.register(
			Registry.ITEM, new Identifier(WackyWhimsicalWonders.MODID, "boxing_glove"),
			new BoxingGloveItem(new QuiltItemSettings()
					.group(ItemGroup.COMBAT)
					.maxCount(1)
			)
	);

	public static final Identifier SPRING_BOXER_ID = new Identifier(WackyWhimsicalWonders.MODID, "spring_boxer");
	public static final Item SPRING_BOXER = Registry.register(
			Registry.ITEM, SPRING_BOXER_ID,
			new SpringBoxerItem(new QuiltItemSettings()
					.group(ItemGroup.COMBAT)
					.maxDamage(200)
					.rarity(Rarity.UNCOMMON)
			)
	);

	public static final Item WRENCH = Registry.register(
			Registry.ITEM, new Identifier(WackyWhimsicalWonders.MODID, "wrench"),
			new WrenchItem(new QuiltItemSettings()
					.group(ItemGroup.TOOLS)
					.maxDamage(ToolMaterials.IRON.getDurability())
			)
	);

	public static final Item FRUIT_BOMB = Registry.register(
			Registry.ITEM, new Identifier(WackyWhimsicalWonders.MODID, "fruit_bomb"),
			new FruitBombItem(new QuiltItemSettings()
					.group(ItemGroup.COMBAT)
					.maxCount(16)
			)
	);

	public static final Item SMOKE_BOMB = Registry.register(
			Registry.ITEM, new Identifier(WackyWhimsicalWonders.MODID, "smoke_bomb"),
			new SmokeBombItem(new QuiltItemSettings()
					.group(ItemGroup.COMBAT)
					.maxCount(16)
			)
	);

	public static final Item SPARKELITE = Registry.register(
			Registry.ITEM, new Identifier(WackyWhimsicalWonders.MODID, "sparkelite"),
			new Item(new QuiltItemSettings()
					.group(ItemGroup.MATERIALS)
			)
	);

	public static final Item SPARKELITE_BOTTLE = Registry.register(
			Registry.ITEM, new Identifier(WackyWhimsicalWonders.MODID, "sparkelite_bottle"),
			new SparkeliteBottleItem(new QuiltItemSettings()
					.group(ItemGroup.BREWING)
					.maxCount(4)
					.recipeRemainder(Items.GLASS_BOTTLE)
			)
	);


	public static void init() {
		// == ITEM BEHAVIORS ==
		DispenserBlock.registerBehavior(SMOKE_BOMB, new ProjectileDispenserBehavior() {
			@Override
			protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
				return Util.make(new SmokeBombEntity(world, position.getX(), position.getY(), position.getZ()), entity -> entity.setItem(stack));
			}
		});
		DispenserBlock.registerBehavior(FRUIT_BOMB, new ProjectileDispenserBehavior() {
			@Override
			protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
				return Util.make(new FruitBombEntity(world, position.getX(), position.getY(), position.getZ()), entity -> entity.setItem(stack));
			}
		});

		modelPredicates();
	}

	// model predicate bullshit
	private static void modelPredicates() {
		ModelPredicateProviderRegistry.register(
				SPRING_BOXER, new Identifier("pull"), (stack, world, entity, seed) -> {
					if (entity == null) {
						return 0.0F;
					} else {
						return SpringBoxerItem.isCharged(stack) ? 0.0F : (float)(stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / (float)SpringBoxerItem.getPullTime(stack);
					}
				}
		);
		ModelPredicateProviderRegistry.register(
				SPRING_BOXER, new Identifier("charged"),
				(stack, world, entity, seed) -> entity != null && SpringBoxerItem.isCharged(stack) ? 1.0F : 0.0F
		);
		ModelPredicateProviderRegistry.register(
				SPRING_BOXER,
				new Identifier("pulling"),
				(stack, world, entity, seed) ->
						entity != null && entity.isUsingItem() && entity.getActiveItem() == stack && !SpringBoxerItem.isCharged(stack) ? 1.0F : 0.0F
		);
	}
}
