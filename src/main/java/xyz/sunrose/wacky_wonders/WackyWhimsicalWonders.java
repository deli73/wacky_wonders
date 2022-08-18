package xyz.sunrose.wacky_wonders;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.UseItemCriterion;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.sunrose.wacky_wonders.blocks.WackyBlocks;
import xyz.sunrose.wacky_wonders.items.WackyItems;

public class WackyWhimsicalWonders implements ModInitializer {
	public static final String MODID = "wacky_wonders";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);


	@Override
	public void onInitialize(ModContainer mod) {
		WackyBlocks.init();
		WackyItems.init();
	}
}
