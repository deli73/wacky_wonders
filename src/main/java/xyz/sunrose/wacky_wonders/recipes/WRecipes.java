package xyz.sunrose.wacky_wonders.recipes;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import xyz.sunrose.wacky_wonders.WWW;

public class WRecipes {
	public static final RecipeType<MachiningRecipe> MACHINING = RecipeType.register(WWW.MODID + "machining");
	public static final RecipeSerializer<MachiningRecipe> MACHINING_SERIALIZER = Registry.register(
			Registry.RECIPE_SERIALIZER,
			WWW.id("machining"),
			new MachiningRecipe.Serializer()
	);

	public static final RecipeType<ElectrifyingRecipe> ELECTRIFYING = RecipeType.register(WWW.MODID + "electrifying");
	public static final RecipeSerializer<ElectrifyingRecipe> ELECTRIFYING_SERIALIZER = Registry.register(
			Registry.RECIPE_SERIALIZER,
			WWW.id("electrifying"),
			new ElectrifyingRecipe.Serializer()
	);

	public static void init(){}
}
