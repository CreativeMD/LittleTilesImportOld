package team.creative.littletilesimportold;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.level.LevelEvent;
import team.creative.creativecore.common.util.type.map.HashMapList;
import team.creative.littletiles.common.block.entity.BETiles;
import team.creative.littletiles.common.block.little.tile.parent.StructureParentCollection;
import team.creative.littletiles.common.block.mc.BlockTile;
import team.creative.littletiles.common.convertion.OldLittleTilesDataParser;
import team.creative.littletiles.common.convertion.OldLittleTilesDataParser.LittleConvertException;
import team.creative.littletiles.common.grid.LittleGrid;

public class OldConverationHandler {
    
    public static HashMapList<Level, OldBETiles> blockEntities = new HashMapList<>();
    private static boolean processing = false;
    private static List<OldBETiles> queued = new ArrayList<>();
    
    public static void add(OldBETiles tiles) {
        if (!tiles.getLevel().isClientSide)
            if (processing)
                synchronized (queued) {
                    queued.add(tiles);
                }
            else
                blockEntities.add(tiles.getLevel(), tiles);
    }
    
    public static void tick(LevelTickEvent event) {
        ArrayList<OldBETiles> blocks = blockEntities.get(event.level);
        if (blocks != null) {
            processing = true;
            for (OldBETiles block : blocks) {
                CompoundTag nbt = block.getOldData().getCompound("content");
                event.level.setBlock(block.getBlockPos(), BlockTile.getState(block.ticking(), block.rendered()), 3);
                BETiles be = BlockTile.loadBE(event.level, block.getBlockPos());
                
                LittleGrid grid = LittleGrid.get(block.getOldData());
                be.convertTo(grid);
                be.updateTiles(x -> {
                    OldLittleTilesDataParser.collect(nbt.getList("tiles", Tag.TAG_COMPOUND), x.noneStructureTiles()::add);
                    ListTag list = nbt.getList("children", Tag.TAG_COMPOUND);
                    
                    for (int i = 0; i < list.size(); i++) {
                        CompoundTag child = list.getCompound(i);
                        try {
                            var structure = x.addStructure(child.getInt("index"), child.getInt("type"));
                            if (child.contains("structure"))
                                structure.setStructureNBT(OldLittleTilesDataParser.convertStructureData(child.getCompound("structure")));
                            else {
                                int[] array = child.getIntArray("coord");
                                if (array.length == 3)
                                    StructureParentCollection.setRelativePos(structure, new BlockPos(array[0], array[1], array[2]));
                                else
                                    throw new LittleConvertException("No valid coord given " + child);
                            }
                            OldLittleTilesDataParser.collect(child.getList("tiles", Tag.TAG_COMPOUND), structure::add);
                        } catch (LittleConvertException e) {
                            e.printStackTrace();
                        }
                    }
                });
                
                be.markDirty();
            }
            blockEntities.removeKey(event.level);
            processing = false;
            synchronized (queued) {
                for (OldBETiles tiles : queued)
                    blockEntities.add(tiles.getLevel(), tiles);
            }
        }
    }
    
    public static void unload(LevelEvent.Unload event) {
        blockEntities.removeKey((Level) event.getLevel());
    }
    
}
