package eu.ansquare.favorite_things.items;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class SpaciousBundleEmpty extends Item {
	public SpaciousBundleEmpty(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
		if(user.getWorld().isClient) return super.useOnEntity(stack, user, entity, hand);
		ItemStack newstack = captureEntity(user, entity);
		if(newstack == null) return ActionResult.FAIL;
		user.setStackInHand(hand, newstack);
		entity.discard();
		return ActionResult.SUCCESS;
	}
	@Nullable
	public ItemStack captureEntity(PlayerEntity user, LivingEntity entity){
		ItemStack newstack = new ItemStack(FavoriteItems.SPACIOUS_BUNDLE_FULL);
		EntityType<?> type = entity.getType();
		if(type.equals(EntityType.ENDER_DRAGON)) return null;
		Identifier entityTypeId = Registries.ENTITY_TYPE.getId(type);
		NbtCompound stackNbt = newstack.getOrCreateNbt();
		stackNbt.putString("entityType", entityTypeId.toString());
		NbtCompound entityNbt = new NbtCompound();
		if(!entity.saveNbt(entityNbt)) {
			user.sendMessage(Text.literal("Failed saving"), true);
			return null;
		}
		stackNbt.put("entityNbt", entityNbt);
		if(entity.hasCustomName()) stackNbt.putString("entityCustomNameString", entity.getCustomName().getString());
		return newstack;
	}

}

