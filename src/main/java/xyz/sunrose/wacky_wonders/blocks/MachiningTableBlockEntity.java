package xyz.sunrose.wacky_wonders.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.networking.api.PlayerLookup;
import xyz.sunrose.wacky_wonders.api.WrenchBoostable;
import xyz.sunrose.wacky_wonders.events.WackySounds;
import xyz.sunrose.wacky_wonders.recipes.MachiningRecipe;
import xyz.sunrose.wacky_wonders.recipes.WackyRecipes;

import java.util.List;

public class MachiningTableBlockEntity extends BlockEntity {
	private final String INGREDIENT_KEY = "item";

	private ItemStack ingredient = ItemStack.EMPTY;
	private List<MachiningRecipe> availableRecipes = Lists.newArrayList();
	private int recipeIndex = 0;
	protected @Nullable MachiningRecipe currentRecipe;

	public Direction facing;

	public MachiningTableBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(WackyBlocks.MACHINING_TABLE_ENTITY_TYPE, blockPos, blockState);
		facing = blockState.get(MachiningTableBlock.FACING);
	}

	public void dropItem(World world, BlockPos pos, ItemStack item){
		ItemEntity drop = new ItemEntity(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, item);
		drop.setPickupDelay(5);
		world.spawnEntity(drop);
	}

	public ItemStack getOutput() {
		if (currentRecipe != null && !currentRecipe.isEmpty()) {
			return currentRecipe.getOutput();
		}
		return ItemStack.EMPTY;
	}

	public void updateRecipes() {
		this.availableRecipes.clear();
		this.currentRecipe = null;
		if (!this.ingredient.isEmpty() && this.getWorld() != null) {
			this.availableRecipes = this.getWorld().getRecipeManager().getAllMatches(WackyRecipes.MACHINING, new SimpleInventory(ingredient), this.world);
			if (this.availableRecipes.size() > 0) {
				this.recipeIndex = 0;
				this.currentRecipe = availableRecipes.get(0);
			}
		}
	}

	public void cycleRecipe() {
		if (!this.availableRecipes.isEmpty()) {
			this.recipeIndex = (this.recipeIndex + 1) % availableRecipes.size(); // loop through all recipes available
			this.currentRecipe = availableRecipes.get(recipeIndex);
		}
	}

	public boolean craft(World world, BlockPos pos) {
		if (currentRecipe != null && !currentRecipe.isEmpty()) {
			if (currentRecipe.matches(new SimpleInventory(ingredient), world)) {
				dropItem(world, pos.up(), currentRecipe.getOutput());
				this.ingredient.decrement(1);
				updateRecipes();
				world.playSound(null, pos, WackySounds.SOUND_CRAFT, SoundCategory.BLOCKS, 1f, 1f);
				return true;
			}
		}
		return false;
	}

	// == NBT MANAGEMENT ==
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		if (nbt.contains(INGREDIENT_KEY, NbtElement.COMPOUND_TYPE)){
			this.setIngredient(ItemStack.fromNbt(nbt.getCompound(INGREDIENT_KEY)));
		}
	}

	@Override
	protected void writeNbt(NbtCompound nbt) {
		super.writeNbt(nbt);
		if (!this.ingredient.isEmpty()){
			nbt.put(INGREDIENT_KEY, this.ingredient.writeNbt(new NbtCompound()));
		}
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		NbtCompound tag = new NbtCompound();
		writeNbt(tag);
		return tag;
	}

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.of(this);
	}

	@Override
	public void markDirty() {
		if(world == null || world.isClient) {return;}
		PlayerLookup.tracking(this).forEach(player -> player.networkHandler.sendPacket(toUpdatePacket()));
		super.markDirty();
		if (!hasWorld()) return;
		if (world.isClient) return;
		world.updateListeners(getPos(), Blocks.AIR.getDefaultState(), getCachedState(), Block.NOTIFY_NEIGHBORS | Block.NOTIFY_LISTENERS);
	}

	// == ETC ==
	public ItemStack getIngredient() {
		return ingredient;
	}

	public void setIngredient(ItemStack ingredient) {
		this.ingredient = ingredient;
		this.updateRecipes();
		this.markDirty();
	}

	public void clear() {
		this.setIngredient(ItemStack.EMPTY);
		this.updateRecipes();
		this.markDirty();
	}
}
