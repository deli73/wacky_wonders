package xyz.sunrose.wacky_wonders;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.UseItemCriterion;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ActionResult;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.sunrose.wacky_wonders.blocks.WackyBlocks;
import xyz.sunrose.wacky_wonders.items.BoxingGloveItem;
import xyz.sunrose.wacky_wonders.items.SpringBoxerItem;
import xyz.sunrose.wacky_wonders.items.WackyItems;
import xyz.sunrose.wacky_wonders.items.WrenchItem;

public class WackyWhimsicalWonders implements ModInitializer {
	public static final String MODID = "wacky_wonders";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);


	@Override
	public void onInitialize(ModContainer mod) {
		WackyBlocks.init();
		WackyItems.init();

		// Events
		AttackEntityCallback.EVENT.register(BoxingGloveItem::onPlayerAttack);
		//UseEntityCallback.EVENT.register(SpringBoxerItem::onPlayerUse);
		AttackBlockCallback.EVENT.register(WrenchItem::onAttackBlock);
	}
}
