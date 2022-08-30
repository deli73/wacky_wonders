package xyz.sunrose.wacky_wonders.client;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import xyz.sunrose.wacky_wonders.blocks.MachiningTableBlock;
import xyz.sunrose.wacky_wonders.blocks.MachiningTableBlockEntity;
import xyz.sunrose.wacky_wonders.util.RotatableVoxelShape;

public class MachiningTableRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
	private final TextRenderer textRenderer;
	public MachiningTableRenderer(BlockEntityRendererFactory.Context ctx){
		this.textRenderer = ctx.getTextRenderer();
	}

	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		MachiningTableBlockEntity table = (MachiningTableBlockEntity) entity;
		BlockState state = table.getCachedState();
		if (state.get(MachiningTableBlock.Y) != 0) { //only render from the bottom half;
		}
		ItemRenderer renderer = MinecraftClient.getInstance().getItemRenderer();
		if (table.getWorld() != null) {
			int lightFront = WorldRenderer.getLightmapCoordinates(table.getWorld(), table.getPos().up());

			// render ingredient
			if (table.getIngredient() != null && !table.getIngredient().isEmpty()) {
				ItemStack ingredient = table.getIngredient();
				Vec3d north_translation = new Vec3d(9.5+3, 16, 2+3);
				Vec3d translation = rotate(north_translation, table.facing);
				Quaternion xRotation = Vec3f.POSITIVE_X.getDegreesQuaternion(90);
				Quaternion yRotation = Vec3f.POSITIVE_Y.getDegreesQuaternion(dirToRotation(table.facing));

				matrices.push();
				matrices.translate(translation.x, translation.y, translation.z);
				matrices.multiply(yRotation);
				matrices.multiply(xRotation);
				matrices.scale(3 / 8f, 3 / 8f, 3 / 8f);
				renderer.renderItem(
						ingredient, ModelTransformation.Mode.FIXED, lightFront, OverlayTexture.DEFAULT_UV,
						matrices, vertexConsumers, 0
				);
				matrices.pop();

				Vec3d north_text_translation = new Vec3d(6, 16.1f, 5);
				Vec3d text_translation = rotate(north_text_translation, table.facing);
				float stringX = -7.5f;
				float stringY = -3.5f;
				matrices.push();
				matrices.translate(text_translation.x, text_translation.y, text_translation.z);
				matrices.multiply(yRotation);
				matrices.multiply(xRotation);
				matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0F));
				matrices.scale(1/64f, 1/64f, 1/64f);
				textRenderer.draw(
						String.format("%02d", ingredient.getCount()), stringX, stringY, 16777215, false,
						matrices.peek().getPosition(), vertexConsumers, false, 0, lightFront
				);
				matrices.pop();
			}

			// render recipe output - TODO render hologram?
			if (table.getOutput() != null && !table.getOutput().isEmpty()){
				ItemStack output = table.getOutput();
				Vec3d north_translation = new Vec3d(4+3, 16+1+3, 8);
				Vec3d translation = rotate(north_translation, table.facing);
				Quaternion rotation = Vec3f.POSITIVE_Y.getDegreesQuaternion(dirToRotation(table.facing));

				matrices.push();
				matrices.translate(translation.x, translation.y, translation.z);
				matrices.multiply(rotation);
				matrices.scale(3 / 8f, 3 / 8f, 3 / 8f);
				renderer.renderItem(
						output, ModelTransformation.Mode.FIXED, lightFront, OverlayTexture.DEFAULT_UV,
						matrices, vertexConsumers, 0
				);
				matrices.pop();

				Quaternion textXRotation = Vec3f.POSITIVE_X.getDegreesQuaternion(180);
				Quaternion textYRotation = Vec3f.POSITIVE_Y.getDegreesQuaternion(dirToRotation(table.facing.getOpposite()));
				matrices.push();
				matrices.translate(translation.x, translation.y, translation.z);
				matrices.multiply(textYRotation);
				matrices.multiply(textXRotation);
				matrices.scale(1/64f, 1/64f, 1/64f);
				renderCount(textRenderer, output, matrices, vertexConsumers, lightFront);
				matrices.pop();
			}
		}
	}

	private float dirToRotation(Direction direction) {
		return switch (direction) {
			case NORTH -> 0;
			case WEST -> 90;
			case SOUTH -> 180;
			case EAST -> -90;
			default -> 360;
		};
	}

	private Vec3d rotate(Vec3d north, Direction direction) {
		return RotatableVoxelShape.rotatedBox(north, north, direction)[0].multiply(1/16d);
	}

	private void renderCount(TextRenderer renderer, ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light){
		if (stack.getCount() != 1) {
			String string = String.valueOf(stack.getCount());
			matrices.translate(0.0, 0.0, 0.0);
			renderer.draw(
					string,
					14 - renderer.getWidth(string),
					8,
					16777215,
					true,
					matrices.peek().getPosition(),
					vertexConsumers,
					false,
					0,
					light
			);
		}
	}

}
