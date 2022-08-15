package io.github.racoondog.legacyapi.mixin.errorreporting;

import io.github.racoondog.legacyapi.utils.ExceptionUtils;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.utils.ReflectInit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(value = ReflectInit.class, remap = false)
public abstract class ReflectInitMixin {
    /**
     * Tries to use getClass().getPackageName() if the addon does not implement getPackage().
     */
    @Redirect(method = "registerPackages", at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/addons/MeteorAddon;getPackage()Ljava/lang/String;"))
    private static String reportOutdatedAddons(MeteorAddon instance) {
        try {
            return instance.getPackage();
        } catch (AbstractMethodError e) {
            return ExceptionUtils.packageFallback(instance);
        }
    }
}
