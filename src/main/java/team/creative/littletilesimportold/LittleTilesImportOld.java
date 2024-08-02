package team.creative.littletilesimportold;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(LittleTilesImportOld.MODID)
public class LittleTilesImportOld {
    
    public static final String MODID = "littletilesimportold";
    public static final Logger LOGGER = LogManager.getLogger(LittleTilesImportOld.MODID);
    
    public LittleTilesImportOld(ModLoadingContext context) {
        var bus = context.getActiveContainer().getEventBus();
        bus.addListener(this::init);
        LittleTilesImportOldRegistry.BLOCKS.register(bus);
        LittleTilesImportOldRegistry.BLOCK_ENTITIES.register(bus);
    }
    
    private void init(final FMLCommonSetupEvent event) {
        NeoForge.EVENT_BUS.addListener(OldConverationHandler::tick);
        NeoForge.EVENT_BUS.addListener(OldConverationHandler::unload);
    }
    
}
