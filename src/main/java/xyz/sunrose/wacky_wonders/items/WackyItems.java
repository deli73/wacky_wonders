package xyz.sunrose.wacky_wonders.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
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

	public static void init(){}
}
