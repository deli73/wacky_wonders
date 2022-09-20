package xyz.sunrose.wacky_wonders.items;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.Util;
import net.minecraft.util.math.Position;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;
import xyz.sunrose.wacky_wonders.WWW;
import xyz.sunrose.wacky_wonders.entities.FruitBombEntity;
import xyz.sunrose.wacky_wonders.entities.SmokeBombEntity;

public class WItems {
	// materials
	public static final Item SPARKELITE = Registry.register(
			Registry.ITEM,  WWW.id("sparkelite"),
			new Item(new QuiltItemSettings()
					.group(WItemGroups.MATERIALS)
					.rarity(Rarity.UNCOMMON)
			)
	);
	// components
	public static final Item SPRING = Registry.register(
			Registry.ITEM, WWW.id("spring"),
			new Item(new QuiltItemSettings()
					.group(WItemGroups.MATERIALS)
			)
	);
	public static final Item SCREW = Registry.register(
			Registry.ITEM, WWW.id("screw"),
			new Item(new QuiltItemSettings()
					.group(WItemGroups.MATERIALS)
			)
	);
	public static final Item COPPER_WIRE = Registry.register(
			Registry.ITEM,  WWW.id("copper_wire"),
			new Item(new QuiltItemSettings()
					.group(WItemGroups.MATERIALS)
			)
	);
	public static final Item COPPER_COIL = Registry.register(
			Registry.ITEM, WWW.id("copper_coil"),
			new Item(new QuiltItemSettings()
					.group(WItemGroups.MATERIALS)
			)
	);
	public static final Item GLASS_LENS = Registry.register(
			Registry.ITEM, WWW.id("glass_lens"),
			new Item(new QuiltItemSettings()
					.group(WItemGroups.MATERIALS)
			)
	);
	public static final Item RUBBER = Registry.register(
			Registry.ITEM, WWW.id("rubber"),
			new Item(new QuiltItemSettings()
					.group(WItemGroups.MATERIALS)
			)
	);

	//tools
	public static final Item WRENCH = Registry.register(
			Registry.ITEM,  WWW.id("wrench"),
			new WrenchItem(new QuiltItemSettings()
					.group(WItemGroups.GADGETS)
					.maxDamage(ToolMaterials.IRON.getDurability())
			)
	);

	public static final Item MALLET = Registry.register(
			Registry.ITEM, WWW.id("mallet"),
			new MalletItem(new QuiltItemSettings()
					.group(WItemGroups.GADGETS)
					.maxDamage(512)
			)
	);

	public static final Item BOXING_GLOVE = Registry.register(
			Registry.ITEM,  WWW.id("boxing_glove"),
			new BoxingGloveItem(new QuiltItemSettings()
					.group(WItemGroups.GADGETS)
					.maxCount(1)
			)
	);
	//fun gadgets
	public static final Identifier SPRING_BOXER_ID =  WWW.id("spring_boxer");
	public static final Item SPRING_BOXER = Registry.register(
			Registry.ITEM, SPRING_BOXER_ID,
			new SpringBoxerItem(new QuiltItemSettings()
					.group(WItemGroups.GADGETS)
					.maxDamage(200)
			)
	);

	public static final Item SMOKE_BOMB = Registry.register(
			Registry.ITEM,  WWW.id("smoke_bomb"),
			new SmokeBombItem(new QuiltItemSettings()
					.group(WItemGroups.GADGETS)
					.maxCount(16)
			)
	);
	public static final Item FRUIT_BOMB = Registry.register(
			Registry.ITEM,  WWW.id("fruit_bomb"),
			new FruitBombItem(new QuiltItemSettings()
					.group(WItemGroups.GADGETS)
					.maxCount(16)
			)
	);

	public static final Item SHRINK_RAY = Registry.register(
			Registry.ITEM, WWW.id("shrink_ray"),
			new ShrinkRayItem(new QuiltItemSettings()
					.group(WItemGroups.GADGETS)
					.maxCount(1)
					.rarity(Rarity.RARE)
			)
	);

	// ???
	public static final Item SPARKELITE_BOTTLE = Registry.register(
			Registry.ITEM,  WWW.id("sparkelite_bottle"),
			new SparkeliteBottleItem(new QuiltItemSettings()
					.group(WItemGroups.ODDITIES)
					.maxCount(4)
					.recipeRemainder(Items.GLASS_BOTTLE)
					.rarity(Rarity.RARE)
			)
	);

	public static final Item GLASSES = Registry.register(
			Registry.ITEM, WWW.id("glasses"),
			new GlassesItem(new QuiltItemSettings()
					.group(WItemGroups.ODDITIES)
					.maxCount(1)
					.equipmentSlot(EquipmentSlot.HEAD)
			)
	);

	public static final Item WISP_BOTTLE = Registry.register(
			Registry.ITEM, WWW.id("wisp_bottle"),
			new Item(new QuiltItemSettings()
					.group(WItemGroups.ODDITIES)
					.maxCount(1)
					.recipeRemainder(Items.GLASS_BOTTLE)
					.rarity(Rarity.UNCOMMON)
			)
	);

	// == INTERNAL ITEMS ==
	public static final Item _WISP_ICON = Registry.register(
			Registry.ITEM, WWW.id("icon_wisp"),
			new Item(new QuiltItemSettings())
	);

	public static void init() {
		WItemGroups.init();

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
