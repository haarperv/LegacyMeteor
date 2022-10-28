package io.github.racoondog.legacymeteor.mixin.errorreporting;

import io.github.racoondog.legacymeteor.LegacyMeteorAddon;
import io.github.racoondog.legacymeteor.utils.ExceptionUtils;
import io.github.racoondog.legacymeteor.utils.PackageUtils;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = MeteorClient.class, remap = false)
public abstract class MeteorClientMixin {
    /**
     * Tries to use getClass().getPackageName() if the addon does not implement getPackage().
     * @author Crosby
     */
    @Redirect(method = "lambda$onInitializeClient$3", at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/addons/MeteorAddon;getPackage()Ljava/lang/String;"))
    private static String reportOutdatedAddons(MeteorAddon instance) {
        return PackageUtils.getPackage(instance);
    }

    /**
     * Print debug info after addon PreInit, Init and PostInit have run.
     * @author Crosby
     */
    @Inject(method = "onInitializeClient", at = @At("TAIL"))
    private void printDebugInfo(CallbackInfo ci) {
        StringBuilder sb = new StringBuilder();
        ExceptionUtils.gatherDebugInfo(sb);
        sb.toString().lines().forEach(LegacyMeteorAddon.LOG::info);
    }
}
