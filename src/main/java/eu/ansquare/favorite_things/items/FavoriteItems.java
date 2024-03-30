package eu.ansquare.favorite_things.items;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonFactory;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeCategory;


import static eu.ansquare.favorite_things.FavoriteThings.REGISTRATE;
public class FavoriteItems {
	public static final ItemEntry<SpaciousBundleEmpty> SPACIOUS_BUNDLE_EMPTY = REGISTRATE
			.item("spacious_bundle_empty", SpaciousBundleEmpty::new)
			.properties(p -> p.maxCount(1))
			.tab(ItemGroups.TOOLS_AND_UTILITIES)
			.recipe((context, provider) -> {
				ShapedRecipeJsonFactory.create(RecipeCategory.TOOLS, context.get())
						.pattern("shs")
						.pattern("hoh")
						.pattern("hhh")
						.ingredient('s', Items.STRING)
						.ingredient('h', Items.RABBIT_HIDE)
						.ingredient('o', Items.CHORUS_FRUIT)
						.criterion(FabricRecipeProvider.hasItem(Items.RABBIT_HIDE), FabricRecipeProvider.conditionsFromItem(Items.RABBIT_HIDE))
						.offerTo(provider);
			})
			.lang("Spacious bundle")
			.register();
	public static final ItemEntry<SpaciousBundleFull> SPACIOUS_BUNDLE_FULL = REGISTRATE
			.item("spacious_bundle_full", SpaciousBundleFull::new)
			.properties(p -> p.maxCount(1))
			.lang("Filled spacious bundle")
			.register();


	public static void init(){
	}
}
