package io.github.racoondog.legacymeteor;

import com.mojang.logging.LogUtils;
import io.github.racoondog.legacymeteor.commands.DevCommands;
import io.github.racoondog.legacymeteor.config.LegacyMeteorSystem;
import io.github.racoondog.legacymeteor.config.LegacyMeteorTab;
import io.github.racoondog.legacymeteor.mixin.ISystems;
import io.github.racoondog.meteorsharedaddonutils.features.TitleScreenCredits;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.gui.tabs.Tabs;
import meteordevelopment.meteorclient.systems.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.HUD;
import meteordevelopment.meteorclient.utils.PostInit;
import meteordevelopment.meteorclient.utils.PreInit;
import meteordevelopment.meteorclient.utils.misc.Version;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.slf4j.Logger;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Environment(EnvType.CLIENT)
public class LegacyMeteorAddon extends MeteorAddon {
    public static final String MOD_ID = "legacy-meteor";
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public static final ModMetadata MOD_META = FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata();
    public static final Logger LOG = LogUtils.getLogger();
    public static LegacyMeteorAddon INSTANCE;
    public static final Version VERSION;

    static {
        String versionString = MOD_META.getVersion().getFriendlyString();
        if (versionString.contains("-")) versionString = versionString.split("-")[0];

        VERSION = new Version(versionString);
    }

    @PreInit
    public static void preInit() {
        ISystems.invokeAdd(new LegacyMeteorSystem());
        ISystems.invokeAdd(new HUD());
    }

    @Override
    public void onInitialize() {
        LOG.info("Initializing LegacyMeteor");

        INSTANCE = this;

        TitleScreenCredits.registerCustomDrawFunction(this, (matrices, credit, y) -> {
            int width1 = mc.textRenderer.getWidth("Legacy");
            int width2 = mc.textRenderer.getWidth("Meteor");
            int width = mc.textRenderer.getWidth(" by ");
            int widthName = mc.textRenderer.getWidth("Crosby");

            int x = mc.currentScreen.width - 3 - width - widthName - width1 - width2;
            y = mc.currentScreen.height - 22;

            mc.textRenderer.drawWithShadow(matrices, "Legacy ", x, y, TitleScreenCredits.WHITE);
            mc.textRenderer.drawWithShadow(matrices, "Meteor", x + width1, y, Color.fromRGBA(186, 18, 0, 255));
            mc.textRenderer.drawWithShadow(matrices, " by ", x + width1 + width2, y, TitleScreenCredits.GRAY);
            mc.textRenderer.drawWithShadow(matrices, "Crosby", x + width1 + width2 + width, y, TitleScreenCredits.WHITE);
        }, false);

        Tabs.add(new LegacyMeteorTab());

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) Commands.get().add( new DevCommands() );
    }

    @PostInit
    public static void postInit() {
        HUD.postInit();
    }

    @Override
    public String getPackage() {
        return "io.github.racoondog.legacymeteor";
    }
}
