package io.github.racoondog.legacyapi;

import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.hud.HUD;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import meteordevelopment.meteorclient.utils.PostInit;
import meteordevelopment.meteorclient.utils.PreInit;
import org.slf4j.Logger;

public class LegacyAPIAddon extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final HudGroup LEGACY = new HudGroup("Legacy");
    public static LegacyAPIAddon INSTANCE;

    @PreInit
    private static void preInit() {
        HUD.preInit();
    }

    @Override
    public void onInitialize() {
        LOG.info("Initializing LegacyAPI");

        INSTANCE = this;
    }

    @PostInit
    private static void postInit() {
        HUD.postInit();
    }

    @Override
    public String getPackage() {
        return "io.github.racoondog.legacyapi";
    }
}
