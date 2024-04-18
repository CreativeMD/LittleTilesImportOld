package team.creative.littletilesimportold.mixin;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.storage.RegionFile;
import team.creative.littletilesimportold.LittleTilesImportOld;

@Mixin(RegionFile.class)
public abstract class RegionFileMixin {
    
    @Shadow
    @Final
    private FileChannel file;
    
    @Inject(method = "getChunkDataInputStream(Lnet/minecraft/world/level/ChunkPos;)Ljava/io/DataInputStream;", at = @At(value = "INVOKE", target = "Ljava/nio/ByteBuffer;get()B",
            shift = Shift.BY, by = 2), require = 1, locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void takeOverIfBufferIsTooSmall(ChunkPos pos, CallbackInfoReturnable<DataInputStream> info, int offset, int sector, int numSector, int length, ByteBuffer originalBuffer, int readLength, byte type) {
        if (readLength - 1 > originalBuffer.remaining()) {
            try {
                LittleTilesImportOld.LOGGER.info("Altering reading chunk data to prevent blocks to get lost " + pos);
                ByteBuffer buffer = ByteBuffer.allocate(readLength);
                this.file.read(buffer, sector * 4096);
                buffer.flip();
                
                //Restore original position
                buffer.getInt();
                buffer.get();
                
                info.setReturnValue(this.createChunkInputStream(pos, type, createStream(buffer, readLength - 1)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
    }
    
    @Shadow
    private static boolean isExternalStreamChunk(byte type) {
        throw new UnsupportedOperationException();
    }
    
    @Shadow
    private DataInputStream createChunkInputStream(ChunkPos p_63651_, byte p_63652_, InputStream p_63653_) throws IOException {
        throw new UnsupportedOperationException();
    }
    
    @Shadow
    private static ByteArrayInputStream createStream(ByteBuffer p_63660_, int p_63661_) {
        throw new UnsupportedOperationException();
    }
}
