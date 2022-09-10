package io.github.racoondog.legacyapi.utils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface AddonInfo {
    String getId();
    void setId(String newId);

    boolean hasRedMark();
    void setRedMark(boolean newRedMark);

    boolean hasUpdatedWidth();
    void setUpdatedWidth(boolean newUpdatedWidth);

    int getWidth();
    void setWidth(int newWidth);

    boolean isOutdated();
    void setOutdated(boolean newOutdated);

    String getVersion();
    void setVersion(String newVersion);
}
