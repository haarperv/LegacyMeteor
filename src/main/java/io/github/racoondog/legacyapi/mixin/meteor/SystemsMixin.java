package io.github.racoondog.legacyapi.mixin.meteor;

import meteordevelopment.meteorclient.systems.System;
import meteordevelopment.meteorclient.systems.Systems;
import meteordevelopment.meteorclient.systems.hud.HUD;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Systems.class, remap = false)
public class SystemsMixin {
    /*
    @Inject(method = "get", at = @At("HEAD"), cancellable = true)
    private static <T extends System<T>> void bruh(Class<T> klass, CallbackInfoReturnable<T> cir) {
        if (klass.equals(HUD.class)) cir.setReturnValue((T) HUD.INSTANCE);
    }
     */
}
