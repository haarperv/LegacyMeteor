package io.github.racoondog.legacymeteor.mixin.errorreporting;

import io.github.racoondog.legacymeteor.utils.AddonInfo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Environment(EnvType.CLIENT)
@Mixin(MeteorAddon.class)
public abstract class MeteorAddonMixin implements AddonInfo {
    @Unique
    private boolean updatedWidth = false;

    @Unique
    private boolean outdated = false;

    @Unique
    private String version;

    @Override
    public boolean hasUpdatedWidth() {
        return this.updatedWidth;
    }

    @Override
    public void setUpdatedWidth(boolean newUpdatedWidth) {
        this.updatedWidth = newUpdatedWidth;
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
