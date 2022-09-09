package io.github.racoondog.legacyapi.config;

import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.tabs.Tab;
import meteordevelopment.meteorclient.gui.tabs.TabScreen;
import meteordevelopment.meteorclient.gui.tabs.WindowTabScreen;
import meteordevelopment.meteorclient.settings.Settings;
import meteordevelopment.meteorclient.utils.misc.NbtUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;

@Environment(EnvType.CLIENT)
public class LegacyAPITab extends Tab {
    public LegacyAPITab() {
        super("Legacy API");
    }

    @Override
    public TabScreen createScreen(GuiTheme theme) {
        return new LegacyAPIScreen(theme, this);
    }

    @Override
    public boolean isScreen(Screen screen) {
        return screen instanceof LegacyAPIScreen;
    }

    public static class LegacyAPIScreen extends WindowTabScreen {
        private final Settings settings;

        public LegacyAPIScreen(GuiTheme theme, Tab tab) {
            super(theme, tab);

            settings = LegacyAPISystem.get().settings;
            settings.onActivated();
        }

        @Override
        public void initWidgets() {
            add(theme.settings(settings)).expandX();
        }

        @Override
        public boolean toClipboard() {
            return NbtUtils.toClipboard(LegacyAPISystem.get());
        }

        @Override
        public boolean fromClipboard() {
            return NbtUtils.fromClipboard(LegacyAPISystem.get());
        }
    }
}
