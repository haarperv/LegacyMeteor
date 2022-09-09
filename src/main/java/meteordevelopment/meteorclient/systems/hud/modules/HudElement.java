package meteordevelopment.meteorclient.systems.hud.modules;

import meteordevelopment.meteorclient.settings.Settings;
import meteordevelopment.meteorclient.systems.hud.HUD;
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
 */
@Environment(EnvType.CLIENT)
public class HudElement implements Snapper.Element, ISerializable<HudElement> {
    public final String name;
    public final String desc;
    public Settings settings = new Settings();

    public HudElement(HUD hud, String name, String desc, boolean bool) {
        this.name = name;
        this.desc = desc;
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
        return -1;
    }

    @Override
    public int getY() {
        return -1;
    }

    @Override
    public int getWidth() {
        return -1;
    }

    @Override
    public int getHeight() {
        return -1;
    }

    @Override
    public void setPos(int x, int y) {
    }

    @Override
    public void move(int deltaX, int deltaY) {
    }

    @Override
    public NbtCompound toTag() {
        return new NbtCompound();
    }

    @Override
    public HudElement fromTag(NbtCompound tag) {
        return this;
    }
}
