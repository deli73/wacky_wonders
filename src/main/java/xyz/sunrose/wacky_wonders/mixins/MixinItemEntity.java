package xyz.sunrose.wacky_wonders.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xyz.sunrose.wacky_wonders.recipes.ElectrifyingRecipe;
import xyz.sunrose.wacky_wonders.recipes.WRecipes;

import java.util.List;

@Mixin(ItemEntity.class)
public abstract class MixinItemEntity extends Entity {
	public MixinItemEntity(EntityType<?> entityType, World world) {
		super(entityType, world);
	}

	@Shadow
	public abstract ItemStack getStack();

	@Shadow
	public abstract void setStack(ItemStack stack);

	@Shadow
	public abstract void setPickupDelay(int pickupDelay);

	@Override
	public void onStruckByLightning(ServerWorld world, LightningEntity lightning) {
		List<ElectrifyingRecipe> recipes = world.getRecipeManager().getAllMatches(WRecipes.ELECTRIFYING, new SimpleInventory(this.getStack()), world);
		if (recipes.size() > 0) {
			ElectrifyingRecipe recipe = recipes.get(0); //THERE CAN ONLY BE ONE (cuz no way to resolve multiple recipes)
			ItemStack oldStack = this.getStack();
			ItemStack output = recipe.getOutput();
			// drop full stacks until the count left is below a stack, then drop that amount here
			int resultCount = output.getCount() * oldStack.getCount();
			int maxCount = output.getMaxCount();
			while (resultCount > output.getMaxCount()) {
				ItemStack dropStack = new ItemStack(output.getItem(), maxCount);
				ItemEntity drop = new ItemEntity(world, getX(), getY(), getZ(), dropStack);
				drop.setPickupDelay(1);
				resultCount -= maxCount;
			}
			this.setInvulnerable(true);
			this.setStack(new ItemStack(output.getItem(), resultCount));
			this.setPickupDelay(1);
			return; // cancel lightning strike
		}

		super.onStruckByLightning(world, lightning);
	}
}
