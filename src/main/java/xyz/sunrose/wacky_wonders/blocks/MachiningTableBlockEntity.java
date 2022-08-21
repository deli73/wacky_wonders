package xyz.sunrose.wacky_wonders.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class MachiningTableBlockEntity extends BlockEntity {
	public MachiningTableBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(WackyBlocks.MACHINING_TABLE_ENTITY_TYPE, blockPos, blockState);
	}
}
