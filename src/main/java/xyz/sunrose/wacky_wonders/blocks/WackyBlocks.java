package xyz.sunrose.wacky_wonders.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;
import xyz.sunrose.wacky_wonders.WackyWhimsicalWonders;

public class WackyBlocks {
	public static final Identifier MACHINING_TABLE_ID = new Identifier(WackyWhimsicalWonders.MODID, "machining_table");
	public static final Block MACHINING_TABLE = Registry.register(
			Registry.BLOCK, MACHINING_TABLE_ID,
			new MachiningTableBlock(QuiltBlockSettings.of(Material.METAL)
					.strength(3.5f)
					.requiresTool()
					.suffocates(
							(blockState, blockView, blockPos) -> blockState.get(MachiningTableBlock.HALF) == DoubleBlockHalf.LOWER
					)
					.solidBlock(
							(blockState, blockView, blockPos) -> blockState.get(MachiningTableBlock.HALF) == DoubleBlockHalf.LOWER
					)
					.sounds(BlockSoundGroup.METAL)
					.allowsSpawning(
							(blockState, blockView, blockPos, object) -> false
					)
			)
	);
	public static final Item MACHINING_TABLE_ITEM = Registry.register(
			Registry.ITEM, MACHINING_TABLE_ID,
			new BlockItem(MACHINING_TABLE, new QuiltItemSettings()
					.group(ItemGroup.DECORATIONS)
			)
	);
	public static final BlockEntityType<MachiningTableBlockEntity> MACHINING_TABLE_ENTITY_TYPE = Registry.register(
			Registry.BLOCK_ENTITY_TYPE, MACHINING_TABLE_ID,
			QuiltBlockEntityTypeBuilder.create(MachiningTableBlockEntity::new, MACHINING_TABLE).build()
	);

	public static void init(){

	}
}
