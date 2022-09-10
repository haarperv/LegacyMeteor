package io.github.racoondog.legacyapi.mixin.errorreporting;

import io.github.racoondog.legacyapi.utils.AddonInfo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
@Mixin(MeteorAddon.class)
public abstract class MeteorAddonMixin implements AddonInfo {
    @Unique
    private String id;

    @Unique
    private boolean redMark = false;

    @Unique
    private boolean updatedWidth = false;

    @Unique
    private int width = 0;

    @Unique
    private boolean outdated = false;

    @Unique
    private String version;

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String newId) {
        this.id = newId;
    }

    @Override
    public boolean hasRedMark() {
        return this.redMark;
    }

    @Override
    public void setRedMark(boolean newRedMark) {
        this.redMark = newRedMark;
    }

    @Override
    public boolean hasUpdatedWidth() {
        return this.updatedWidth;
    }

    @Override
    public void setUpdatedWidth(boolean newUpdatedWidth) {
        this.updatedWidth = newUpdatedWidth;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public void setWidth(int newWidth) {
        this.width = newWidth;
    }

    @Override
    public boolean isOutdated() {
        return this.outdated;
    }

    @Override
    public void setOutdated(boolean newOutdated) {
        this.outdated = newOutdated;
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    @Override
    public void setVersion(String newVersion) {
        this.version = newVersion;
    }
}
