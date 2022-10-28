package io.github.racoondog.legacymeteor.utils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface AddonInfo {
    boolean hasUpdatedWidth();
    void setUpdatedWidth(boolean newUpdatedWidth);

    boolean isOutdated();
    void setOutdated(boolean newOutdated);

    String getVersion();
    void setVersion(String newVersion);
}
