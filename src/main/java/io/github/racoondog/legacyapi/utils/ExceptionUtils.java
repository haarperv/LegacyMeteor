package io.github.racoondog.legacyapi.utils;

import com.google.common.collect.Lists;
import io.github.racoondog.legacyapi.LegacyAPIAddon;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.impl.util.ExceptionUtil;

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
        } else {
            loggedExceptions.put(hashCode, Lists.newArrayList(type));
        }
        return true;
    }

    public static String packageFallback(MeteorAddon addon, AbstractMethodError e) {
        try {
            String packageName = addon.getClass().getPackageName();
            if (logException(addon.name, AddonExceptionType.PACKAGE_NAME)) LegacyAPIAddon.LOG.error("'{}' does not implement abstract method 'getPackage'.\nHowever, a fallback has been used to allow the mod to run.", addon.name);
            return packageName;
        } catch (Throwable t) {
            ExceptionUtils.throwException(e, "'%s' does not support the current version of Meteor Client and needs to be updated.".formatted(addon.name));
        }
        return null;
    }

    public static void duplicateRegistration(String packagePrefix) {
        String[] tokens = packagePrefix.split("\\.");
        String name = tokens[tokens.length - 1];
        if (logException(name, AddonExceptionType.DUPLICATE_REGISTRATION)) LegacyAPIAddon.LOG.warn("Prevented duplicate lambda factory registration from Meteor addon '{}'.", name);
    }

    private enum AddonExceptionType {
        PACKAGE_NAME,
        DUPLICATE_REGISTRATION
    }
}
