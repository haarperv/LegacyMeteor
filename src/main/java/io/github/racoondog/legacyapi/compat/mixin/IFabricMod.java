package io.github.racoondog.legacyapi.compat.mixin;

import com.terraformersmc.modmenu.util.mod.fabric.FabricMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Pseudo
@Mixin(FabricMod.class)
public interface IFabricMod {
    @Accessor("metadata")
    @NotNull ModMetadata getMetadata();
}
