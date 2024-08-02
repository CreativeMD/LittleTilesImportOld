package team.creative.littletilesimportold.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.mojang.serialization.Dynamic;
import com.mojang.serialization.OptionalDynamic;

@Mixin(targets = "net.minecraft.util.datafix.fixes.ItemStackComponentizationFix.ItemStackData")
public interface ItemStackDataAccessor {
    
    @Accessor
    public String getItem();
    
    @Accessor
    @Mutable
    public void setItem(String value);
    
    @Accessor
    public Dynamic<?> getTag();
    
    @Accessor
    public void setTag(Dynamic<?> value);
    
    @Invoker
    public OptionalDynamic<?> callRemoveTag(String id);
    
    @Invoker
    public void callSetComponent(String id, Dynamic<?> dynamic);
    
}
