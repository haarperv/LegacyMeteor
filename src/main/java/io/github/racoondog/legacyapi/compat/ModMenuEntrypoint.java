package io.github.racoondog.legacyapi.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.racoondog.legacyapi.config.LegacyAPITab;
import meteordevelopment.meteorclient.gui.GuiThemes;
import meteordevelopment.meteorclient.gui.tabs.Tabs;

public class ModMenuEntrypoint implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            for (var tab : Tabs.get()) {
                if (tab instanceof LegacyAPITab) return tab.createScreen(GuiThemes.get());
            }
            return Tabs.get().get(0).createScreen(GuiThemes.get());
        };
    }
}
