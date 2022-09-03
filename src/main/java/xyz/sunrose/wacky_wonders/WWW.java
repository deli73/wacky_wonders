package xyz.sunrose.wacky_wonders;

import com.unascribed.lib39.dessicant.api.DessicantControl;
import com.unascribed.lib39.ripple.api.SplashTextRegistry;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.sunrose.wacky_wonders.advancements.WCriteria;
import xyz.sunrose.wacky_wonders.blocks.WBlocks;
import xyz.sunrose.wacky_wonders.entities.WEntities;
import xyz.sunrose.wacky_wonders.events.WSounds;
import xyz.sunrose.wacky_wonders.items.BoxingGloveItem;
import xyz.sunrose.wacky_wonders.items.MalletItem;
import xyz.sunrose.wacky_wonders.items.WItems;
import xyz.sunrose.wacky_wonders.items.WrenchItem;
import xyz.sunrose.wacky_wonders.recipes.WRecipes;

public class WWW implements ModInitializer {
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
				"Drone racing!", "Nope!", "The Flattening!",
				"Also try Raft!", "Also try Marble It Up!", "Also try Hyperbolica!", "Also try Garry's Mod!"
		);
		SplashTextRegistry.replace("Conventional!", "Unconventional!");
		SplashTextRegistry.replace("Absolutely no memes!", "Slightly more memes than advertised!");

		//initialize blocks and items
		WSounds.init();
		WBlocks.init();
		WItems.init();
		WEntities.init();
		WRecipes.init();
		WCriteria.init();

		// register Events
		AttackEntityCallback.EVENT.register(BoxingGloveItem::onPlayerAttack);
		AttackEntityCallback.EVENT.register(MalletItem::onPlayerAttack);
		AttackBlockCallback.EVENT.register(WrenchItem::onAttackBlock);
	}

	public static Identifier id(String name){
		return new Identifier(MODID, name);
	}
}
