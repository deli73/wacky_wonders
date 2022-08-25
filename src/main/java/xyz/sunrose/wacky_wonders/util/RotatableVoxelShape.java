package xyz.sunrose.wacky_wonders.util;

import net.minecraft.block.Block;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.ArrayList;
import java.util.List;

/**
 * We have no clue how this works tbh
 * fixed up by feline (thank you so much oh my gods): https://git.sleeping.town/feline
 */
public class RotatableVoxelShape {
	List<VoxelShape> shapes;

	public RotatableVoxelShape(double minX, double minY, double minZ, double maxX, double maxY, double maxZ){
		shapes = new ArrayList<>();
		Vec3d min = new Vec3d(minX, minY, minZ);
		Vec3d max = new Vec3d(maxX, maxY, maxZ);

		// DOWN: N/A
		shapes.add(VoxelShapes.empty());
		// UP: N/A
		shapes.add(VoxelShapes.empty());
		// NORTH: 0°
		shapes.add(Block.createCuboidShape(minX, minY, minZ, maxX, maxY, maxZ));
		// SOUTH: 180°
		Vec3d[] s = rotatedPoint(min, max, Direction.SOUTH);
		shapes.add(Block.createCuboidShape(s[0].x, s[0].y, s[0].z, s[1].x, s[1].y, s[1].z));
		// EAST: 90°
		Vec3d[] e = rotatedPoint(min, max, Direction.EAST);
		shapes.add(Block.createCuboidShape(e[0].x, e[0].y, e[0].z, e[1].x, e[1].y, e[1].z));
		// WEST: 270°
		Vec3d[] w = rotatedPoint(min, max, Direction.WEST);
		shapes.add(Block.createCuboidShape(w[0].x, w[0].y, w[0].z, w[1].x, w[1].y, w[1].z));
	}

	public VoxelShape getShape(Direction direction){
		return shapes.get(direction.getId());
	}

	private Vec3d[] rotatedPoint(Vec3d min, Vec3d max, Direction direction){
		return switch (direction) {
			case DOWN, UP, NORTH -> new Vec3d[]{min, max};
			case SOUTH -> new Vec3d[]{new Vec3d(16d - max.x, min.y, 16d - max.z), new Vec3d(16d - min.x, max.y, 16d - min.z)};
			case WEST -> new Vec3d[]{new Vec3d(16d - max.z, min.y, min.x), new Vec3d(16d - min.z, max.y, max.x)};
			case EAST -> new Vec3d[]{new Vec3d(min.z, min.y, 16d - max.x), new Vec3d(max.z, max.y, 16d - min.x)};
		};
	}
}
