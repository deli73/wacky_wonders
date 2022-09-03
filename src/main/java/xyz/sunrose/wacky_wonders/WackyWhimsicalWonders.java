package xyz.sunrose.wacky_wonders;

import com.unascribed.lib39.dessicant.api.DessicantControl;
import com.unascribed.lib39.ripple.api.SplashTextRegistry;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.sunrose.wacky_wonders.advancements.WackyCriteria;
import xyz.sunrose.wacky_wonders.blocks.WackyBlocks;
import xyz.sunrose.wacky_wonders.entities.WackyEntities;
import xyz.sunrose.wacky_wonders.events.WackySounds;
import xyz.sunrose.wacky_wonders.items.BoxingGloveItem;
import xyz.sunrose.wacky_wonders.items.WackyItems;
import xyz.sunrose.wacky_wonders.items.WrenchItem;
import xyz.sunrose.wacky_wonders.recipes.WackyRecipes;

public class WackyWhimsicalWonders implements ModInitializer {
	public static final String MODID = "wacky_wonders";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize(ModContainer mod) {
		// activate lib39 features
		DessicantControl.optIn(MODID);
		// add/tweak custom splash texts!
		SplashTextRegistry.registerStatic(
				"Cartoon logic!", "Spring-loaded!", "Yeetable!", "Stimmy!", "Hackable!", "Wacky!", "Whimsical!",
				"Lathes are dangerous!", "First-order retrievabiity!", "Myth BUSTED", "Engineering!",
				"Also try Raft!", "Also try Marble It Up!", "Also try Hyperbolica!", "Also try Garry's Mod!"
		);
		SplashTextRegistry.replace("Conventional!", "Unconventional!");
		SplashTextRegistry.replace("Absolutely no memes!", "Slightly more memes than advertised!");

		//initialize blocks and items
		WackySounds.init();
		WackyBlocks.init();
		WackyItems.init();
		WackyEntities.init();
		WackyRecipes.init();
		WackyCriteria.init();

		// register Events
		AttackEntityCallback.EVENT.register(BoxingGloveItem::onPlayerAttack);
		AttackBlockCallback.EVENT.register(WrenchItem::onAttackBlock);
	}
}
