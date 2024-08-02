package team.creative.littletilesimportold.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.serialization.Dynamic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.item.ItemStack;
import team.creative.littletiles.LittleTilesRegistry;
import team.creative.littletiles.common.convertion.OldLittleTilesDataParser;
import team.creative.littletiles.common.convertion.OldLittleTilesDataParser.LittleConvertException;
import team.creative.littletiles.common.item.ItemLittleBlueprint;

@Mixin(ItemStack.class)
public class ItemStackComponentizationFixMixin {
    
    @Inject(at = @At("TAIL"), require = 1,
            method = "fixItemStack(Lnet/minecraft/util/datafix/fixes/ItemStackComponentizationFix$ItemStackData;Lcom/mojang/serialization/Dynamic;)V")
    private static void fixItemStack(@Coerce Object data, Dynamic<?> dynamic, CallbackInfo info) {
        ItemStackDataAccessor d = (ItemStackDataAccessor) data;
        String id = d.getItem();
        if (id.equalsIgnoreCase("littletiles:recipe") || id.equalsIgnoreCase("littletiles:recipeadvanced")) {
            d.setItem(LittleTilesRegistry.BLUEPRINT.getRegisteredName());
            CompoundTag tag = (CompoundTag) d.getTag().cast(NbtOps.INSTANCE);
            if (!tag.isEmpty()) {
                try {
                    CompoundTag content = OldLittleTilesDataParser.convert(tag);
                    tag = new CompoundTag();
                    tag.put(ItemLittleBlueprint.CONTENT_KEY, content);
                    d.setTag(new Dynamic<>(NbtOps.INSTANCE, tag));
                } catch (LittleConvertException e) {
                    e.printStackTrace();
                }
                
            }
        } else if (id.equalsIgnoreCase("littletiles:multiTiles")) {
            d.setItem(LittleTilesRegistry.ITEM_TILES.getRegisteredName());
            CompoundTag tag = (CompoundTag) d.getTag().cast(NbtOps.INSTANCE);
            if (!tag.isEmpty())
                try {
                    d.setTag(new Dynamic<>(NbtOps.INSTANCE, OldLittleTilesDataParser.convert(tag)));
                } catch (LittleConvertException e) {
                    e.printStackTrace();
                }
        }
    }
    
}
