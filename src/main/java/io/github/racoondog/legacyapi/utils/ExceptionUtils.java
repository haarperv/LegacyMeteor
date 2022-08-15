package io.github.racoondog.legacyapi.utils;

import com.google.common.collect.Lists;
import io.github.racoondog.legacyapi.LegacyAPIAddon;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.impl.util.ExceptionUtil;

import java.io.IOException;
import java.util.List;

@Environment(EnvType.CLIENT)
public final class ExceptionUtils {
    private static void throwException(Throwable t, String cause) {
        RuntimeException exception = ExceptionUtil.gatherExceptions(t, null, exc -> new RuntimeException(String.format(cause), exc));

        if (exception != null) {
            throw exception;
        }
    }

    private static final Int2ObjectMap<List<AddonExceptionType>> loggedExceptions = new Int2ObjectOpenHashMap<>();
    private static <T> boolean logException(T key, AddonExceptionType type) {
        int hashCode = key.hashCode();
        if (loggedExceptions.containsKey(hashCode)) {
            List<AddonExceptionType> list = loggedExceptions.get(hashCode);
            if (list.contains(type)) return false;
            list.add(type);
        } else loggedExceptions.put(hashCode, Lists.newArrayList(type));
        return true;
    }

    private static boolean logException(AddonExceptionType type) {
        return logException(LegacyAPIAddon.INSTANCE, type);
    }

    public static String packageFallback(MeteorAddon addon) {
        String packageName = addon.getClass().getPackageName();
        if (logException(addon.name, AddonExceptionType.PACKAGE_NAME)) {
            LegacyAPIAddon.LOG.error("Using a fallback 'getPackage' implementation for Meteor addon '{}'.", addon.name);
            AddonInfo.get(addon).outdated = true;
        }
        return packageName;
    }

    public static void duplicateRegistration(String packageName) {
        packageName = PackageUtils.getLastDirectory(packageName);
        if (logException(packageName, AddonExceptionType.DUPLICATE_REGISTRATION)) {
            LegacyAPIAddon.LOG.warn("Prevented duplicate lambda factory registration from Meteor addon '{}'.", packageName);
            AddonInfo.fromPackage(packageName).outdated = true;
        }
    }

    public static void windowIconError(IOException e) {
        if (logException(AddonExceptionType.WINDOW_ICON_ERROR)) {
            LegacyAPIAddon.LOG.error("Could not return window icon to the default value.");
            LegacyAPIAddon.LOG.trace(e.toString());
        }
    }

    public static void legacyHud() {
        if (logException(AddonExceptionType.LEGACY_HUD)) {
            LegacyAPIAddon.LOG.warn("Using the legacy HUD system for compatibility reasons. Expect frequent errors.");
        }
    }

    public static void rateLimit() {
        if (logException(AddonExceptionType.GITHUB_API_RATE_LIMIT)) LegacyAPIAddon.LOG.warn("Could not check for addon updates due to reaching the rate limit.");
    }

    private enum AddonExceptionType {
        PACKAGE_NAME,
        DUPLICATE_REGISTRATION,
        WINDOW_ICON_ERROR,
        LEGACY_HUD,
        GITHUB_API_RATE_LIMIT
    }
}
