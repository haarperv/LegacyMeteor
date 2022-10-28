package io.github.racoondog.legacymeteor.mixin.errorreporting;

import io.github.racoondog.legacymeteor.utils.ExceptionUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.crash.CrashReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(CrashReport.class)
public abstract class CrashReportMixin {
    /**
     * Adds LegacyAPI debug info to crash log.
     * @author Crosby
     */
    @Inject(method = "addStackTrace", at = @At("TAIL"))
    private void onAddStackTrace(StringBuilder sb, CallbackInfo info) {
        sb.append('\n');
        ExceptionUtils.gatherDebugInfo(sb);
    }
}
