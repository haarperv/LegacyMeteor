package io.github.racoondog.legacyapi.mixin.errorreporting;

import io.github.racoondog.legacyapi.utils.ExceptionUtils;
import meteordevelopment.orbit.EventBus;
import meteordevelopment.orbit.listeners.LambdaListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(value = EventBus.class, remap = false)
public abstract class EventBusMixin {
    @Unique private static final List<String> registeredPackages = new ArrayList<>();

    @Inject(method = "registerLambdaFactory", at = @At("HEAD"), cancellable = true)
    private void preventDuplicateRegistration(String packagePrefix, LambdaListener.Factory factory, CallbackInfo ci) {
        if (registeredPackages.contains(packagePrefix)) {
            ExceptionUtils.duplicateRegistration(packagePrefix);
            ci.cancel();
        }
        registeredPackages.add(packagePrefix);
    }
}
