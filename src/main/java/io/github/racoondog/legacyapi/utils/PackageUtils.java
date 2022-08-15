package io.github.racoondog.legacyapi.utils;

import meteordevelopment.meteorclient.addons.MeteorAddon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class PackageUtils {
    public static String getLastDirectory(String packageName) {
        String[] tokens = packageName.split("\\.");
        return tokens[tokens.length - 1];
    }

    public static String getPackage(MeteorAddon addon) {
        try {
            return addon.getPackage();
        } catch (AbstractMethodError e) {
            return ExceptionUtils.packageFallback(addon);
        }
    }
}
