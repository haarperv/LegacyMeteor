package io.github.racoondog.legacyapi.mixin.errorreporting.hudspoof;

import meteordevelopment.meteorclient.systems.hud.HudRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(HudRenderer.class)
public abstract class HudRendererMixin {
    /**
     * Readds the old addPostTask method.
     */
    public void addPostTask(Runnable task) {
        HudRenderer.INSTANCE.post(task);
    }
}
