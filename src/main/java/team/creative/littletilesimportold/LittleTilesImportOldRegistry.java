package team.creative.littletilesimportold;

import java.util.function.Supplier;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import team.creative.littletiles.LittleTiles;
import team.creative.littletilesimportold.OldBETiles.OldBETilesRendered;
import team.creative.littletilesimportold.OldBETiles.OldBETilesTicking;
import team.creative.littletilesimportold.OldBETiles.OldBETilesTickingRendered;

public class LittleTilesImportOldRegistry {
    
    // BLOCKS
    
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, LittleTiles.MODID);
    
    public static final Holder<Block> OLD_BLOCK_TILES = BLOCKS.register("blocklittletiles", () -> new Block(Properties.of()));
    public static final Holder<Block> OLD_BLOCK_TILES_TICKING = BLOCKS.register("blocklittletilesticking", () -> new Block(Properties.of()));
    public static final Holder<Block> OLD_BLOCK_TILES_RENDERED = BLOCKS.register("blocklittletilesrendered", () -> new Block(Properties.of()));
    public static final Holder<Block> OLD_BLOCK_TILES_TICKING_RENDERED = BLOCKS.register("blocklittletilestickingrendered", () -> new Block(Properties.of()));
    
    // BLOCK_ENTITY
    
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, "minecraft");
    
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<OldBETiles>> OLD_BE_TILES_TYPE = registerBlockEntity("littletilestileentity",
        () -> BlockEntityType.Builder.of(OldBETiles::new, OLD_BLOCK_TILES.value()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<OldBETilesTicking>> OLD_BE_TILES_TYPE_TICKING = registerBlockEntity("littletilestileentityticking",
        () -> BlockEntityType.Builder.of(OldBETilesTicking::new, OLD_BLOCK_TILES.value()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<OldBETilesRendered>> OLD_BE_TILES_TYPE_RENDERED = registerBlockEntity("littletilestileentityrendered",
        () -> BlockEntityType.Builder.of(OldBETilesRendered::new, OLD_BLOCK_TILES.value()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<OldBETilesTickingRendered>> OLD_BE_TILES_TYPE_TICKING_RENDERED = registerBlockEntity(
        "littletilestileentitytickingrendered", () -> BlockEntityType.Builder.of(OldBETilesTickingRendered::new, OLD_BLOCK_TILES.value()));
    
    public static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> registerBlockEntity(String name, Supplier<BlockEntityType.Builder<T>> sup) {
        return BLOCK_ENTITIES.register(name, () -> sup.get().build(Util.fetchChoiceType(References.BLOCK_ENTITY, name)));
    }
    
}
