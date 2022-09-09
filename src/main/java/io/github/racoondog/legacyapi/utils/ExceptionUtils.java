package io.github.racoondog.legacyapi.utils;

import com.google.common.collect.Lists;
import io.github.racoondog.legacyapi.LegacyAPIAddon;
import meteordevelopment.meteorclient.addons.AddonManager;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Environment(EnvType.CLIENT)
public final class ExceptionUtils {
    public static void gatherDebugInfo(StringBuilder sb) {
        sb.append("-- Legacy API --\n");
        sb.append("Version: ").append(LegacyAPIAddon.VERSION).append('\n');

        if (!AddonManager.ADDONS.isEmpty()) {
            sb.append("Addons:\n");
            for (var addon : AddonManager.ADDONS) {
                sb.append("- ").append(addon.name);
                String commit = addon.getCommit();
                if (commit != null && !commit.isBlank()) sb.append("; ").append(commit);
                sb.append('\n');
            }
        }

        if (hasExceptions()) {
            sb.append("Exceptions: \n");
            for (var entry : loggedExceptions.entrySet()) {
                appendExceptions(sb, entry.getKey(), entry.getValue());
            }
        }
    }

    private static final HashMap<Object, List<AddonExceptionType>> loggedExceptions = new HashMap<>();
    private static final List<GlobalExceptionType> globalExceptions = new ArrayList<>();

    private static boolean hasExceptions() {
        if (loggedExceptions.isEmpty()) return false;
        for (var list : loggedExceptions.values()) {
            if (!list.isEmpty()) return true;
        }
        return false;
    }

    private static void appendExceptions(StringBuilder sb, Object key, List<AddonExceptionType> exceptionList) {
        if (exceptionList.isEmpty()) return;
        String keyName = key instanceof MeteorAddon ma ? ma.name : key.toString();
        sb.append("- ").append(keyName).append(":\n");
        for (var exceptionType : exceptionList) {
            sb.append("  - ").append(exceptionType.description).append('\n');
        }
    }

    public static void logGlobalException(GlobalExceptionType type) {
        if (!globalExceptions.contains(type)) globalExceptions.add(type);
    }


    @SuppressWarnings("unchecked")
    public static <T> void logException(T key, AddonExceptionType type) {
        if (key instanceof String str) {
            key = (T) PackageUtils.getLastDirectory(str);
        } else if (key instanceof Class<?> clazz) {
            key = (T) PackageUtils.getLastDirectory(PackageUtils.trimPackageName(clazz.getPackageName()));
        }

        if (loggedExceptions.containsKey(key)) {
            List<AddonExceptionType> list = loggedExceptions.get(key);
            if (!list.contains(type)) list.add(type);
        } else loggedExceptions.put(key, Lists.newArrayList(type));
    }

    public enum AddonExceptionType {
        PACKAGE_NAME("Using fallback 'getPackage' implementation"),
        DUPLICATE_REGISTRATION("Duplicate lambda factory registration"),
        OUTDATED_COMMAND_ARGUMENT_TYPE("Outdated command argument type"),
        LEGACY_HUD("Using legacy HUD system");

        public final String description;

        AddonExceptionType(String description) {
            this.description = description;
        }
    }

    public enum GlobalExceptionType {
        WINDOW_ICON_ERROR("Could not return window icon to the default value"),
        GITHUB_API_RATE_LIMIT("Could not check for addon updates due to reaching the rate limit");

        public final String description;

        GlobalExceptionType(String description) {
            this.description = description;
        }
    }
}
