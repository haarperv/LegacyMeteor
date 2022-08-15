package io.github.racoondog.legacyapi.utils;

import meteordevelopment.meteorclient.addons.AddonManager;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.utils.PreInit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.HashMap;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Environment(EnvType.CLIENT)
public final class AddonInfo {
    private static final HashMap<MeteorAddon, AddonInfo> addonMap = new HashMap<>();
    private static final int DEFAULT_WIDTH = mc.textRenderer.getWidth("Meteor Client by MineGame159, squidoodly & seasnail");

    public static AddonInfo get(MeteorAddon addon) {
        if (!addonMap.containsKey(addon)) {
            AddonInfo info = new AddonInfo();
            addonMap.put(addon, info);
            return info;
        }
        return addonMap.get(addon);
    }

    public boolean redMark = false;
    public boolean updatedWidth = false;
    public int width = DEFAULT_WIDTH;
    public boolean outdated = false;

    private static final HashMap<String, AddonInfo> packageMap = new HashMap<>();

    public static AddonInfo fromPackage(String packageName) {
        return packageMap.get(packageName);
    }

    @PreInit
    public static void preInit() {
        for (var addon : AddonManager.ADDONS) {
            String packageName = PackageUtils.getLastDirectory(PackageUtils.getPackage(addon));

            if (addonMap.containsKey(addon)) packageMap.put(packageName, addonMap.get(addon));
            else {
                AddonInfo info = new AddonInfo();
                addonMap.put(addon, info);
                packageMap.put(packageName, info);
            }
        }
    }
}
