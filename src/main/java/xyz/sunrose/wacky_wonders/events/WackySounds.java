package xyz.sunrose.wacky_wonders.events;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import xyz.sunrose.wacky_wonders.WackyWhimsicalWonders;

public class WackySounds {
	private static final Identifier SOUND_BOOST_ID = new Identifier(WackyWhimsicalWonders.MODID, "wrench.boost");
	public static final SoundEvent SOUND_BOOST = Registry.register(
			Registry.SOUND_EVENT, SOUND_BOOST_ID,
			new SoundEvent(SOUND_BOOST_ID)
	);

	private static final Identifier SOUND_CRAFT_ID = new Identifier(WackyWhimsicalWonders.MODID, "machining_table.craft");
	public static final SoundEvent SOUND_CRAFT = Registry.register(
			Registry.SOUND_EVENT, SOUND_CRAFT_ID,
			new SoundEvent(SOUND_CRAFT_ID)
	);

	public static void init() {}
}