package eu.ansquare.favorite_things.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpaciousBundleFull extends Item {
	public SpaciousBundleFull(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		ItemStack stack = context.getStack();
		if(context.getWorld().isClient) return super.useOnBlock(context);
		if(!containsEntity(stack)) return ActionResult.FAIL;
		Vec3d posToSpawnAt = context.getHitPos();
		NbtCompound nbt = stack.getNbt();
		Entity entity = getEntityFromData(context.getWorld(), posToSpawnAt, nbt);
		if(entity == null) return ActionResult.FAIL;
		context.getWorld().spawnEntity(entity);
		context.getPlayer().setStackInHand(context.getHand(), new ItemStack(FavoriteItems.SPACIOUS_BUNDLE_EMPTY));
		return ActionResult.SUCCESS;
	}
	@Nullable
	public Entity getEntityFromData(World world, Vec3d pos, NbtCompound nbt){
		EntityType<?> type = Registries.ENTITY_TYPE.get(new Identifier(nbt.getString("entityType")));
		Entity entity = type.create(world);
		if(entity == null) return null;
		entity.readNbt(nbt.getCompound("entityNbt"));
		entity.updatePosition(pos.getX(), pos.getY(), pos.getZ());
		entity.setPos(pos.getX(), pos.getY(), pos.getZ());
		//if(nbt.contains("entityCustomNameString")) entity.setCustomName(Text.literal(nbt.getString("entityCustomNameString")));
		return entity;
	}
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		NbtCompound nbt = stack.getOrCreateNbt();
		if(containsEntity(stack)){
			MutableText entityTypeText = (MutableText) Registries.ENTITY_TYPE.get(new Identifier(nbt.getString("entityType"))).getName();
			tooltip.add(entityTypeText.formatted(Formatting.AQUA));
			if(nbt.contains("entityCustomNameString")){
				tooltip.add(Text.literal(nbt.getString("entityCustomNameString")).formatted(Formatting.AQUA, Formatting.ITALIC));
			}

		}
	}
	public static boolean containsEntity(ItemStack stack){
		return stack.getOrCreateNbt().contains("entityType");
	}
}

