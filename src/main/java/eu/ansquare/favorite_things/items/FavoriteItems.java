package eu.ansquare.favorite_things.items;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.dispenser.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.data.server.recipe.ShapedRecipeJsonFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;


import static eu.ansquare.favorite_things.FavoriteThings.REGISTRATE;
import static eu.ansquare.favorite_things.items.SpaciousBundleFull.containsEntity;

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
			.onRegister(spaciousBundleFull -> {
				DispenserBlock.registerBehavior(spaciousBundleFull, new ItemDispenserBehavior() {
					public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
						Direction direction = (Direction)pointer.getBlockState().get(DispenserBlock.FACING);
						if(pointer.getWorld().isClient) return stack;
						if(!containsEntity(stack)) return stack;
						Vec3d posToSpawnAt = pointer.getPos().offset(direction).ofCenter();
						NbtCompound nbt = stack.getNbt();
						Entity entity = spaciousBundleFull.getEntityFromData(pointer.getWorld(), posToSpawnAt, nbt);
						if(entity == null) return stack;
						pointer.getWorld().spawnEntity(entity);
						pointer.getWorld().emitGameEvent(null, GameEvent.ENTITY_PLACE, entity.getPos());
						return new ItemStack(FavoriteItems.SPACIOUS_BUNDLE_EMPTY);
					}
				});
			})
			.lang("Filled spacious bundle")
			.register();


	public static void init(){
	}
}
