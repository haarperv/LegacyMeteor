package io.github.racoondog.legacyapi.utils;

import com.google.common.collect.Lists;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.List;
import java.util.Locale;

@Environment(EnvType.CLIENT)
public final class PackageUtils {
    public static String getLastDirectory(String packageName) {
        String[] tokens = packageName.split("\\.");
        return tokens[tokens.length - 1];
    }

    private static final List<String> packagesToSkip = Lists.newArrayList("commands", "command", "systems", "huds", "hud");

    public static String trimPackageName(String packageName) {
        String[] tokens = packageName.split("\\.");
        int limit = tokens.length - 1;
        while (packagesToSkip.contains(tokens[limit].toLowerCase(Locale.ROOT))) limit--;
        StringBuilder sb = new StringBuilder();
        int index = 0;
        while (index <= limit) {
            sb.append(tokens[index]);
            if (index != limit) {
                sb.append('.');
                index++;
            } else {
                break;
            }
        }
        return sb.toString();
    }

    public static String getPackage(MeteorAddon addon) {
        try {
            return addon.getPackage();
        } catch (AbstractMethodError e) {
            ExceptionUtils.logException(addon, ExceptionUtils.AddonExceptionType.PACKAGE_NAME);
            return addon.getClass().getPackageName();
        }
    }
}
