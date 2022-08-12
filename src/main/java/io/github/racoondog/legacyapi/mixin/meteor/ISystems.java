package io.github.racoondog.legacyapi.mixin.meteor;

import meteordevelopment.meteorclient.systems.System;
import meteordevelopment.meteorclient.systems.Systems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Environment(EnvType.CLIENT)
@Mixin(value = Systems.class, remap = false)
public interface ISystems {
    @Accessor
    static Map<Class<? extends System>, System<?>> getSystems() {
        throw new AssertionError();
    }

    @Invoker
    static System<?> invokeAdd(System<?> system) {
        throw new AssertionError();
    }
}
