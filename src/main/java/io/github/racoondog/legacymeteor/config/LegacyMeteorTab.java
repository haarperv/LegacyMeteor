package io.github.racoondog.legacymeteor.config;

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
public class LegacyMeteorTab extends Tab {
    public LegacyMeteorTab() {
        super("Legacy Meteor");
    }

    @Override
    public TabScreen createScreen(GuiTheme theme) {
        return new LegacyMeteorScreen(theme, this);
    }

    @Override
    public boolean isScreen(Screen screen) {
        return screen instanceof LegacyMeteorScreen;
    }

    public static class LegacyMeteorScreen extends WindowTabScreen {
        private final Settings settings;

        public LegacyMeteorScreen(GuiTheme theme, Tab tab) {
            super(theme, tab);

            settings = LegacyMeteorSystem.get().settings;
            settings.onActivated();
        }

        @Override
        public void initWidgets() {
            add(theme.settings(settings)).expandX();
        }

        @Override
        public boolean toClipboard() {
            return NbtUtils.toClipboard(LegacyMeteorSystem.get());
        }

        @Override
        public boolean fromClipboard() {
            return NbtUtils.fromClipboard(LegacyMeteorSystem.get());
        }
    }
}
