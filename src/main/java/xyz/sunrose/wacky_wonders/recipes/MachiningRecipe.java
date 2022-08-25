package xyz.sunrose.wacky_wonders.recipes;

import com.google.gson.JsonObject;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class MachiningRecipe implements Recipe<Inventory> {
	private final Identifier id;
	private final String group;
	private final Ingredient ingredient;
	private final ItemStack output;
	public MachiningRecipe(Identifier id, String group, Ingredient ingredient, ItemStack output) {
		this.id = id;
		this.group = group;
		this.ingredient = ingredient;
		this.output = output;
	}

	@Override
	public RecipeType<?> getType() {
		return WackyRecipes.MACHINING;
	}

	@Override
	public boolean matches(Inventory inventory, World world) {
		return ingredient.test(inventory.getStack(0));
	}

	@Override
	public ItemStack craft(Inventory inventory) {
		return output.copy();
	}

	@Override
	public boolean fits(int width, int height) {
		return width * height == 1;
	}

	@Override
	public DefaultedList<Ingredient> getIngredients() {
		return DefaultedList.copyOf(Ingredient.EMPTY, ingredient);
	}

	@Override
	public ItemStack getOutput() {
		return output;
	}

	@Override
	public Identifier getId() {
		return id;
	}

	@Override
	public String getGroup() {
		return group;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return WackyRecipes.MACHINING_SERIALIZER;
	}

	public static class Serializer implements RecipeSerializer<MachiningRecipe> {

		@Override
		public MachiningRecipe read(Identifier id, JsonObject json) {
			String group = JsonHelper.getString(json, "group", "");
			Ingredient ingredient = Ingredient.fromJson(JsonHelper.getObject(json, "ingredient"));
			ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "result"));
			return new MachiningRecipe(id, group, ingredient, output);
		}

		@Override
		public MachiningRecipe read(Identifier id, PacketByteBuf buf) {
			String group = buf.readString();
			Ingredient ingredient = Ingredient.fromPacket(buf);
			ItemStack output = buf.readItemStack();
			return new MachiningRecipe(id, group, ingredient, output);
		}

		@Override
		public void write(PacketByteBuf buf, MachiningRecipe recipe) {
			buf.writeString(recipe.group);
			recipe.ingredient.write(buf);
			buf.writeItemStack(recipe.output);
		}
	}

}
