package team.creative.littletilesimportold.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import team.creative.littletilesimportold.OldBETiles;

@Mixin(LevelChunk.class)
public class LevelChunkMixin {
    
    @Inject(method = "setBlockEntity(Lnet/minecraft/world/level/block/entity/BlockEntity;)V", at = @At("HEAD"), require = 1)
    public void setBlockEntity(BlockEntity entity, CallbackInfo info) {
        if (entity instanceof OldBETiles o) {
            ((LevelChunk) (Object) this).setBlockState(entity.getBlockPos(), o.getValidState(), false);
            o.setBlockState(o.getValidState());
        }
    }
}
