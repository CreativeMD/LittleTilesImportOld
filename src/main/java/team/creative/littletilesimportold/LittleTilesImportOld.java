package team.creative.littletilesimportold;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(LittleTilesImportOld.MODID)
public class LittleTilesImportOld {
    
    public static final String MODID = "littletilesimportold";
    public static final Logger LOGGER = LogManager.getLogger(LittleTilesImportOld.MODID);
    
    public LittleTilesImportOld() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
        LittleTilesImportOldRegistry.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        LittleTilesImportOldRegistry.BLOCK_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    private void init(final FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.addListener(OldConverationHandler::tick);
        MinecraftForge.EVENT_BUS.addListener(OldConverationHandler::unload);
    }
    
}
