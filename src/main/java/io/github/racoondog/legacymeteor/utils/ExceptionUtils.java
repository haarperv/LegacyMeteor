package io.github.racoondog.legacymeteor.utils;

import com.google.common.collect.Lists;
import io.github.racoondog.legacymeteor.LegacyMeteorAddon;
import io.github.racoondog.meteorsharedaddonutils.utils.AddonUtils;
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
        sb.append("-- Legacy Meteor --\n");
        sb.append("Version: ").append(LegacyMeteorAddon.VERSION).append('\n');

        if (!AddonManager.ADDONS.isEmpty()) {
            sb.append("Addons:\n");
            for (var addon : AddonManager.ADDONS) {
                sb.append("- ").append(addon.name).append("; v").append(AddonUtils.getAddonVersion(addon));
                try {
                    String commit = addon.getCommit();
                    if (commit != null && !commit.isBlank()) sb.append("; commit ").append(commit);
                } catch (Exception e) {
                    ExceptionUtils.logGlobalException(GlobalExceptionType.GITHUB_API_RATE_LIMIT);
                }
                sb.append('\n');
            }
        }

        if (hasExceptions()) {
            sb.append("Exceptions:\n");
            for (var entry : loggedExceptions.entrySet()) {
                appendExceptions(sb, entry.getKey(), entry.getValue());
            }
        }

        if (!globalExceptions.isEmpty()) {
            sb.append("Global Exceptions:\n");
            for (var exception : globalExceptions) {
                sb.append("- ").append(exception.description).append('\n');
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

        //Transform key into either a MeteorAddon object or a relevant string identifier
        if (key instanceof String str) {
            MeteorAddon addon = fromString(str);
            key = addon == null ? (T) PackageUtils.getLastDirectory(PackageUtils.trimPackageName(str)) : (T) addon;
        } else if (key instanceof Class<?> clazz) {
            String pkg = clazz.getPackageName();
            MeteorAddon addon = fromString(pkg);
            key = addon == null ? (T) PackageUtils.getLastDirectory(PackageUtils.trimPackageName(pkg)) : (T) addon;
        }

        if (key instanceof MeteorAddon addon) {
            ((AddonInfo) addon).setOutdated(true);
        }

        if (loggedExceptions.containsKey(key)) {
            List<AddonExceptionType> list = loggedExceptions.get(key);
            if (!list.contains(type)) list.add(type);
        } else loggedExceptions.put(key, Lists.newArrayList(type));
    }

    private static MeteorAddon fromString(String string) {
        if (string.contains(".")) {
            for (var addon : AddonManager.ADDONS) {
                if (string.startsWith(PackageUtils.getPackage(addon))) return addon;
            }
        } else {
            for (var addon : AddonManager.ADDONS) {
                if (AddonUtils.getAddonId(addon).equals(string)) return addon;
            }
        }
        return null;
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
