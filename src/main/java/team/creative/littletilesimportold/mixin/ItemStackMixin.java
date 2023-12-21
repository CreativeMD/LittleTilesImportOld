package team.creative.littletilesimportold.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import team.creative.littletiles.LittleTilesRegistry;
import team.creative.littletiles.common.convertion.OldLittleTilesDataParser;
import team.creative.littletiles.common.convertion.OldLittleTilesDataParser.LittleConvertException;
import team.creative.littletiles.common.item.ItemLittleBlueprint;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    
    @Inject(method = "of(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/world/item/ItemStack;", at = @At("HEAD"), require = 1)
    private static void ofInject(CompoundTag nbt, CallbackInfoReturnable<ItemStack> info) {
        String id = nbt.getString("id");
        if (id.equalsIgnoreCase("littletiles:recipe") || id.equalsIgnoreCase("littletiles:recipeadvanced")) {
            nbt.putString("id", LittleTilesRegistry.BLUEPRINT.getId().toString());
            if (nbt.contains("tag", Tag.TAG_COMPOUND)) {
                CompoundTag tag = nbt.getCompound("tag");
                try {
                    CompoundTag content = OldLittleTilesDataParser.convert(tag);
                    nbt.put("tag", tag = new CompoundTag());
                    tag.put(ItemLittleBlueprint.CONTENT_KEY, content);
                    
                } catch (LittleConvertException e) {
                    e.printStackTrace();
                }
                
            }
        } else if (id.equalsIgnoreCase("littletiles:multiTiles")) {
            if (nbt.contains("tag", Tag.TAG_COMPOUND)) {
                nbt.putString("id", LittleTilesRegistry.ITEM_TILES.getId().toString());
                try {
                    nbt.put("tag", OldLittleTilesDataParser.convert(nbt.getCompound("tag")));
                } catch (LittleConvertException e) {
                    e.printStackTrace();
                }
                
            }
        }
    }
    
}
