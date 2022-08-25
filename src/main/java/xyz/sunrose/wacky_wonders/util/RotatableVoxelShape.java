package xyz.sunrose.wacky_wonders.util;

import net.minecraft.block.Block;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.ArrayList;
import java.util.List;

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
		Vec3d s_p1 = rotatedPoint(min, Direction.SOUTH);
		Vec3d s_p2 = rotatedPoint(max, Direction.SOUTH);
		double s_minX = Math.min(s_p1.x, s_p2.x);
		double s_maxX = Math.max(s_p1.x, s_p2.x);
		double s_minZ = Math.min(s_p1.z, s_p2.z);
		double s_maxZ = Math.max(s_p1.z, s_p2.z);
		shapes.add(Block.createCuboidShape(s_minX, s_p1.y, s_minZ, s_maxX, s_p2.y, s_maxZ));
		// EAST: 270°
		Vec3d e_p1 = rotatedPoint(min, Direction.EAST);
		Vec3d e_p2 = rotatedPoint(max, Direction.EAST);
		double e_minX = Math.min(e_p1.x, e_p2.x);
		double e_maxX = Math.max(e_p1.x, e_p2.x);
		double e_minZ = Math.min(e_p1.z, e_p2.z);
		double e_maxZ = Math.max(e_p1.z, e_p2.z);
		shapes.add(Block.createCuboidShape(e_minX, e_p1.y, e_minZ, e_maxX, e_p2.y, e_maxZ));
		// WEST: 90°
		Vec3d w_p1 = rotatedPoint(min, Direction.WEST);
		Vec3d w_p2 = rotatedPoint(max, Direction.WEST);
		double w_minX = Math.min(w_p1.x, w_p2.x);
		double w_maxX = Math.max(w_p1.x, w_p2.x);
		double w_minZ = Math.min(w_p1.z, w_p2.z);
		double w_maxZ = Math.max(w_p1.z, w_p2.z);
		shapes.add(Block.createCuboidShape(w_minX, w_p1.y, w_minZ, w_maxX, w_p2.y, w_maxZ));
	}

	public VoxelShape getShape(Direction direction){
		return shapes.get(direction.getId());
	}

	private Vec3d rotatedPoint(Vec3d input, Direction direction){


		Vec3d in_offset = input.add(8, 0, 8);
		return switch (direction){
			case DOWN, UP -> Vec3d.ZERO;
			case NORTH-> input;
			case SOUTH-> in_offset.rotateY(180).add(-8, 0, -8);
			case EAST -> in_offset.rotateY(90).add(-8, 0, -8);
			case WEST -> in_offset.rotateY(270).add(-8, 0, -8);
		};
	}
}
