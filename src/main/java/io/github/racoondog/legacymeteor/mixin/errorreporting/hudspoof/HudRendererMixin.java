package io.github.racoondog.legacymeteor.mixin.errorreporting.hudspoof;

import meteordevelopment.meteorclient.systems.hud.HudRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(HudRenderer.class)
public abstract class HudRendererMixin {
    /**
     * Spoofs the old addPostTask method.
     * @author Crosby
     */
    public void addPostTask(Runnable task) {
        //Does nothing
    }
}
