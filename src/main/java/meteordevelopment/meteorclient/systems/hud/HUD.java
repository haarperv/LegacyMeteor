package meteordevelopment.meteorclient.systems.hud;

import io.github.racoondog.legacyapi.LegacyAPIAddon;
import io.github.racoondog.legacyapi.mixin.meteor.ISystems;
import meteordevelopment.meteorclient.systems.System;
import meteordevelopment.meteorclient.systems.hud.modules.HudElement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class HUD extends System<HUD> {
    public static final HUD INSTANCE = new HUD();
    public final List<HudElement> elements = new ArrayList<>();

    public static void preInit() {
        ISystems.getSystems().put(INSTANCE.getClass(), INSTANCE);
    }
    public HUD() {
        super("HUD-placeholder-do-not-use");
    }

    public static void postInit() {
        if (INSTANCE.elements.isEmpty()) return;

        LegacyAPIAddon.LOG.error("Using legacy Meteor HUD system for compatibility reasons. Expect frequent errors.");

        for (var element : INSTANCE.elements) {
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
