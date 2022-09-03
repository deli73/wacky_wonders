package xyz.sunrose.wacky_wonders.items;

import com.unascribed.lib39.fractal.api.ItemSubGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import org.quiltmc.qsl.item.group.api.QuiltItemGroup;
import xyz.sunrose.wacky_wonders.WWW;

public class WItemGroups {
	public static final QuiltItemGroup PARENT = QuiltItemGroup.builder(WWW.id("parent"))
			.icon(() -> new ItemStack(WItems.SPRING_BOXER))
			.build();
	public static final ItemSubGroup BLOCKS = ItemSubGroup.create(PARENT, WWW.id("blocks"));
	public static final ItemSubGroup MATERIALS = ItemSubGroup.create(PARENT, WWW.id("materials"));
	public static final ItemSubGroup GADGETS = ItemSubGroup.create(PARENT, WWW.id("gadgets"));
	public static final ItemSubGroup ODDITIES = ItemSubGroup.create(PARENT, WWW.id("oddities"));

	public static void init() {}
}
