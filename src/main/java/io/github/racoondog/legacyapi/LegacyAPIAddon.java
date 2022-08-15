package io.github.racoondog.legacyapi;

import com.mojang.logging.LogUtils;
import io.github.racoondog.legacyapi.config.LegacyAPISystem;
import io.github.racoondog.legacyapi.config.LegacyAPITab;
import io.github.racoondog.legacyapi.mixin.ISystems;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.gui.tabs.Tabs;
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
    public static void preInit() {
        ISystems.invokeAdd(new HUD());
    }

    @Override
    public void onInitialize() {
        LOG.info("Initializing LegacyAPI");

        INSTANCE = this;

        ISystems.invokeAdd(new LegacyAPISystem());
        Tabs.add(new LegacyAPITab());
    }

    @PostInit
    public static void postInit() {
        HUD.postInit();
    }

    @Override
    public String getPackage() {
        return "io.github.racoondog.legacyapi";
    }
}
