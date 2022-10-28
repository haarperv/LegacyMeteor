package io.github.racoondog.legacymeteor.mixin.errorreporting;

import io.github.racoondog.legacymeteor.utils.ExceptionUtils;
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

    /**
     * Stops outdated addons from registering to events in their initializers.
     * @author Crosby
     */
    @Inject(method = "registerLambdaFactory", at = @At("HEAD"), cancellable = true)
    private void preventDuplicateRegistration(String packagePrefix, LambdaListener.Factory factory, CallbackInfo ci) {
        if (registeredPackages.contains(packagePrefix)) {
            ExceptionUtils.logException(packagePrefix, ExceptionUtils.AddonExceptionType.DUPLICATE_REGISTRATION);
            ci.cancel();
        }
        registeredPackages.add(packagePrefix);
    }
}
