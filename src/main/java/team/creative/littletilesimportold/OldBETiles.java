package team.creative.littletilesimportold;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import team.creative.creativecore.common.be.BlockEntityCreative;

public class OldBETiles extends BlockEntityCreative {
    
    private CompoundTag oldData;
    public boolean processed = false;
    
    public OldBETiles(BlockPos pos, BlockState state) {
        this(LittleTilesImportOldRegistry.OLD_BE_TILES_TYPE.get(), pos, state);
    }
    
    public OldBETiles(BlockEntityType<? extends OldBETiles> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    public boolean ticking() {
        return false;
    }
    
    public boolean rendered() {
        return false;
    }
    
    public CompoundTag getOldData() {
        return oldData;
    }
    
    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        OldConverationHandler.add(this);
    }
    
    @Override
    public boolean isValidBlockState(BlockState state) {
        return true;
    }
    
    @Override
    public void handleUpdate(CompoundTag nbt, boolean chunkUpdate) {}
    
    @Override
    protected void loadAdditional(CompoundTag nbt, Provider provider) {
        this.oldData = nbt.copy();
        super.loadAdditional(nbt, provider);
    }
    
    public BlockState getValidState() {
        return LittleTilesImportOldRegistry.OLD_BLOCK_TILES.value().defaultBlockState();
    }
    
    public static class OldBETilesTicking extends OldBETiles {
        
        public OldBETilesTicking(BlockPos pos, BlockState state) {
            super(LittleTilesImportOldRegistry.OLD_BE_TILES_TYPE_TICKING.get(), pos, state);
        }
        
        @Override
        public boolean ticking() {
            return true;
        }
        
        @Override
        public BlockState getValidState() {
            return LittleTilesImportOldRegistry.OLD_BLOCK_TILES_TICKING.value().defaultBlockState();
        }
        
    }
    
    public static class OldBETilesRendered extends OldBETiles {
        
        public OldBETilesRendered(BlockPos pos, BlockState state) {
            super(LittleTilesImportOldRegistry.OLD_BE_TILES_TYPE_RENDERED.get(), pos, state);
        }
        
        @Override
        public boolean rendered() {
            return true;
        }
        
        @Override
        public BlockState getValidState() {
            return LittleTilesImportOldRegistry.OLD_BLOCK_TILES_RENDERED.value().defaultBlockState();
        }
        
    }
    
    public static class OldBETilesTickingRendered extends OldBETiles {
        
        public OldBETilesTickingRendered(BlockPos pos, BlockState state) {
            super(LittleTilesImportOldRegistry.OLD_BE_TILES_TYPE_TICKING_RENDERED.get(), pos, state);
        }
        
        @Override
        public boolean ticking() {
            return true;
        }
        
        @Override
        public boolean rendered() {
            return true;
        }
        
        @Override
        public BlockState getValidState() {
            return LittleTilesImportOldRegistry.OLD_BLOCK_TILES_TICKING_RENDERED.value().defaultBlockState();
        }
        
    }
    
}
