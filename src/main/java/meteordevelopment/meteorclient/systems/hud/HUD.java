package meteordevelopment.meteorclient.systems.hud;

import io.github.racoondog.legacyapi.LegacyAPIAddon;
import io.github.racoondog.legacyapi.utils.ExceptionUtils;
import meteordevelopment.meteorclient.systems.System;
import meteordevelopment.meteorclient.systems.hud.modules.HudElement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Spoofing old Meteor Client code in order to make addons using the old HUD system boot.
 */
@Environment(EnvType.CLIENT)
public class HUD extends System<HUD> {
    public static HUD INSTANCE;
    public final List<HudElement> elements = new ArrayList<>();

    public HUD() {
        super("HUD-placeholder-do-not-use");
        INSTANCE = this;
    }

    public static void postInit() {
        if (INSTANCE.elements.isEmpty()) return;

        for (var element : INSTANCE.elements) {
            ExceptionUtils.legacyHud();
            Hud.get().register(new HudElementInfo<>(LegacyAPIAddon.LEGACY, element.name + "-UNSTABLE", element.desc, element::create));
        }
    }

    @Override
    public void save(File folder) {}

    @Override
    public void save() {}

    @Override
    public void load(File folder) {}

    @Override
    public void load() {}
}
