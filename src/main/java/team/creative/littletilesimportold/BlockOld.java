package team.creative.littletilesimportold;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockOld extends Block implements EntityBlock {
    
    public BlockOld(Properties properties) {
        super(properties);
    }
    
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new OldBETiles(pos, state);
    }
    
}
