package xyz.sunrose.wacky_wonders.items;

import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;
import xyz.sunrose.wacky_wonders.WackyWhimsicalWonders;

public class WackyItems {
	public static final Identifier SPRING_BOXER_ID = new Identifier(WackyWhimsicalWonders.MODID, "spring_boxer");
	public static final Item SPRING_BOXER = Registry.register(
			Registry.ITEM, SPRING_BOXER_ID,
			new SpringBoxerItem(new QuiltItemSettings()
					.group(ItemGroup.COMBAT)
					.maxDamage(200)
					.rarity(Rarity.UNCOMMON)
			)
	);


	public static void init() {
		//model predicate bullshit
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
