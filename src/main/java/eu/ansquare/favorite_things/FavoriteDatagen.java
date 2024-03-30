package eu.ansquare.favorite_things;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.function.Consumer;

public class FavoriteDatagen implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FavoriteThings.LOGGER.info("Initializing data generator");
		Path resources = Paths.get(System.getProperty(ExistingFileHelper.EXISTING_RESOURCES));
		ExistingFileHelper helper = ExistingFileHelper.withResources(resources);

		FavoriteThings.REGISTRATE.setupDatagen(fabricDataGenerator.createPack(), helper);
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(DatagenTestAdvancementsProvider::new);
	}
	static class DatagenTestAdvancementsProvider extends FabricAdvancementProvider {
		protected DatagenTestAdvancementsProvider(FabricDataOutput output) {
			super(output);
		}


		@Override
		public void generateAdvancement(Consumer<Advancement> consumer) {
			Advancement rootAdvancement = Advancement.Task.create()
					.display(
							Items.DIRT, // The display icon
							Text.literal("Your test"), // The title
							Text.literal("Now test"), // The description
							new Identifier("textures/gui/advancements/backgrounds/adventure.png"), // Background image used
							AdvancementFrame.TASK.TASK, // Options: TASK, CHALLENGE, GOAL
							true, // Show toast top right
							true, // Announce to chat
							false // Hidden in the advancement tab
					)
					// The first string used in criterion is the name referenced by other advancements when they want to have 'requirements'
					.criterion("got_dirt", InventoryChangedCriterion.Conditions.items(Items.DIRT))
					.build(consumer, FavoriteThings.ID + "/root");
		}
	}
}
