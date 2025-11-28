package team.creative.littletilesimportold;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.level.LevelEvent;
import team.creative.littletiles.common.block.entity.BETiles;
import team.creative.littletiles.common.block.little.tile.parent.StructureParentCollection;
import team.creative.littletiles.common.block.mc.BlockTile;
import team.creative.littletiles.common.convertion.OldLittleTilesDataParser;
import team.creative.littletiles.common.convertion.OldLittleTilesDataParser.LittleConvertException;
import team.creative.littletiles.common.grid.LittleGrid;

public class OldConverationHandler {
    
    public static Map<Level, Map<BlockPos, OldBETiles>> blockEntities = new Object2ObjectArrayMap<>();
    private static final int LOGUPDATE = 100;
    private static volatile int TOTAL_QUEUED = 0;
    private static int COUNTER = 0;
    private static int TOTAL = 0;
    private static boolean processing = false;
    private static List<OldBETiles> queued = new ArrayList<>();
    
    private static Map<BlockPos, OldBETiles> getOrCreate(Level level) {
        var map = blockEntities.get(level);
        if (map != null)
            return map;
        blockEntities.put(level, map = new Object2ObjectArrayMap<>());
        return map;
    }
    
    public static void add(OldBETiles tiles) {
        if (tiles.processed)
            return;
        if (!tiles.getLevel().isClientSide) {
            if (processing)
                synchronized (queued) {
                    queued.add(tiles);
                }
            else
                getOrCreate(tiles.getLevel()).put(tiles.getBlockPos(), tiles);
            TOTAL_QUEUED++;
        }
    }
    
    public static void tick(LevelTickEvent event) {
        if (event.phase == Phase.START)
            return;
        var level = event.level;
        Map<BlockPos, OldBETiles> blocks = blockEntities.get(level);
        if (blocks != null) {
            processing = true;
            int j = 0;
            for (OldBETiles block : blocks.values()) {
                CompoundTag nbt = block.getOldData().getCompound("content");
                event.level.setBlock(block.getBlockPos(), BlockTile.getState(block.ticking(), block.rendered()), 3);
                BETiles be = BlockTile.loadBE(event.level, block.getBlockPos());
                
                LittleGrid grid = LittleGrid.get(block.getOldData());
                if (!be.isEmpty())
                    System.out.println(be.getBlockPos() + " is receiving another update");
                be.convertTo(grid);
                be.updateTiles(x -> {
                    OldLittleTilesDataParser.collect(nbt.getList("tiles", Tag.TAG_COMPOUND), x.noneStructureTiles()::add);
                    ListTag list = nbt.getList("children", Tag.TAG_COMPOUND);
                    
                    for (int i = 0; i < list.size(); i++) {
                        CompoundTag child = list.getCompound(i);
                        try {
                            var structure = x.addStructure(child.getInt("index"), child.getInt("type"));
                            if (child.contains("structure")) {
                                CompoundTag converted;
                                try {
                                    converted = OldLittleTilesDataParser.convertStructureData(child.getCompound("structure"));
                                } catch (OldLittleTilesDataParser.LittleMissingStructureException e) {
                                    converted = child.getCompound("structure");
                                    converted.putString("id_former", converted.getString("id"));
                                    converted.putString("id", "fixed");
                                    converted = OldLittleTilesDataParser.convertStructureData(converted);
                                }
                                structure.setStructureNBT(converted);
                            } else {
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
                block.processed = true;
                COUNTER--;
                TOTAL++;
                j++;
                if (COUNTER < 0) {
                    LittleTilesImportOld.LOGGER.info("Converted {}/{} (total {}/{})", j, blocks.size(), TOTAL, TOTAL_QUEUED);
                    COUNTER = LOGUPDATE;
                }
            }
            blockEntities.remove(level);
            processing = false;
            synchronized (queued) {
                for (OldBETiles tiles : queued)
                    if (!tiles.processed)
                        getOrCreate(tiles.getLevel()).put(tiles.getBlockPos(), tiles);
            }
        }
    }
    
    public static void unload(LevelEvent.Unload event) {
        blockEntities.remove(event.getLevel());
    }
    
}
