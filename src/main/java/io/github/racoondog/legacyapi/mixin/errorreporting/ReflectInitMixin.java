package io.github.racoondog.legacyapi.mixin.errorreporting;

import io.github.racoondog.legacyapi.utils.PackageUtils;
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
     * @author Crosby
     */
    @Redirect(method = "registerPackages", at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/addons/MeteorAddon;getPackage()Ljava/lang/String;"))
    private static String reportOutdatedAddons(MeteorAddon instance) {
        return PackageUtils.getPackage(instance);
    }
}
