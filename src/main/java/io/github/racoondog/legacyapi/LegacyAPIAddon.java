package io.github.racoondog.legacyapi;

import com.mojang.logging.LogUtils;
import io.github.racoondog.legacyapi.commands.DevCommands;
import io.github.racoondog.legacyapi.config.LegacyAPISystem;
import io.github.racoondog.legacyapi.config.LegacyAPITab;
import io.github.racoondog.legacyapi.mixin.ISystems;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.gui.tabs.Tabs;
import meteordevelopment.meteorclient.systems.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.HUD;
import meteordevelopment.meteorclient.utils.PostInit;
import meteordevelopment.meteorclient.utils.PreInit;
import meteordevelopment.meteorclient.utils.misc.Version;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class LegacyAPIAddon extends MeteorAddon {
    public static final String MOD_ID = "legacy-api";
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public static final ModMetadata MOD_META = FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata();
    public static final Logger LOG = LogUtils.getLogger();
    public static LegacyAPIAddon INSTANCE;
    public static final Version VERSION;

    static {
        String versionString = MOD_META.getVersion().getFriendlyString();
        if (versionString.contains("-")) versionString = versionString.split("-")[0];

        VERSION = new Version(versionString);
    }

    @PreInit
    public static void preInit() {
        ISystems.invokeAdd(new LegacyAPISystem());
        ISystems.invokeAdd(new HUD());
    }

    @Override
    public void onInitialize() {
        LOG.info("Initializing LegacyAPI");

        INSTANCE = this;

        Tabs.add(new LegacyAPITab());

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) Commands.get().add( new DevCommands() );
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
