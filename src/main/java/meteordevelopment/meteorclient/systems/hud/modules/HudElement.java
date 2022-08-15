package meteordevelopment.meteorclient.systems.hud.modules;

import io.github.racoondog.legacyapi.LegacyAPIAddon;
import meteordevelopment.meteorclient.settings.Settings;
import meteordevelopment.meteorclient.systems.hud.HUD;
import meteordevelopment.meteorclient.systems.hud.HudElementInfo;
import meteordevelopment.meteorclient.systems.hud.HudRenderer;
import meteordevelopment.meteorclient.systems.hud.screens.HudEditorScreen;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.misc.ISerializable;
import meteordevelopment.meteorclient.utils.other.Snapper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.NbtCompound;

/**
 * Spoofing old Meteor Client code in order to make addons using the old HUD system boot.
 * Works as a wrapper for the new HudElement object.
 */
@Environment(EnvType.CLIENT)
public class HudElement implements Snapper.Element, ISerializable<HudElement> {

    public final HudElementInfo<meteordevelopment.meteorclient.systems.hud.HudElement> INFO;
    public final String name;
    public final String desc;
    public final InnerHudElement inner = (InnerHudElement) create();
    public Settings settings = inner.settings;

    public HudElement(HUD hud, String name, String desc, boolean bool) {
        this.name = name;
        this.desc = desc;

        INFO = new HudElementInfo<>(LegacyAPIAddon.LEGACY, name, desc, () -> inner);
    }

    public meteordevelopment.meteorclient.systems.hud.HudElement create() {
        return new InnerHudElement(INFO, this);
    }

    public void update() {}

    public void tick(HudRenderer renderer) {}

    public void render(HudRenderer renderer) {}

    public void onFontChanged() {}

    public boolean isInEditor() {
        return !Utils.canUpdate() || HudEditorScreen.isOpen();
    }

    @Override
    public int getX() {
        return inner.getX();
    }

    @Override
    public int getY() {
        return inner.getY();
    }

    @Override
    public int getWidth() {
        return inner.getWidth();
    }

    @Override
    public int getHeight() {
        return inner.getHeight();
    }

    @Override
    public void setPos(int x, int y) {
        inner.setPos(x, y);
    }

    @Override
    public void move(int deltaX, int deltaY) {
        inner.move(deltaX, deltaY);
    }

    @Override
    public NbtCompound toTag() {
        return inner.toTag();
    }

    @Override
    public HudElement fromTag(NbtCompound tag) {
        inner.fromTag(tag);
        return this;
    }

    public static class InnerHudElement extends meteordevelopment.meteorclient.systems.hud.HudElement {
        private final HudElement outer;

        public InnerHudElement(HudElementInfo<?> info, HudElement outer) {
            super(info);
            this.outer = outer;
        }

        @Override
        public void updatePos() {
            outer.update();
        }

        @Override
        public void tick(HudRenderer renderer) {
            outer.tick(renderer);
        }

        @Override
        public void render(HudRenderer renderer) {
            outer.render(renderer);
        }

        @Override
        public void onFontChanged() {
            outer.onFontChanged();
        }
    }
}
