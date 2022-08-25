package xyz.sunrose.wacky_wonders.recipes;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import xyz.sunrose.wacky_wonders.WackyWhimsicalWonders;

public class WackyRecipes {
	public static final RecipeType<MachiningRecipe> MACHINING = RecipeType.register(WackyWhimsicalWonders.MODID + "machining");
	public static final RecipeSerializer<MachiningRecipe> MACHINING_SERIALIZER = Registry.register(
			Registry.RECIPE_SERIALIZER,
			new Identifier(WackyWhimsicalWonders.MODID, "machining"),
			new MachiningRecipe.Serializer()
	);

	public static void init(){}
}
