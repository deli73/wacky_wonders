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
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod name as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MODID = "wacky_wonders";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);


	// various registry things


	@Override
	public void onInitialize(ModContainer mod) {
		//LOGGER.info("Hello Quilt world from {}!", mod.metadata().name());
		WackyBlocks.init();
		WackyItems.init();
	}
}
