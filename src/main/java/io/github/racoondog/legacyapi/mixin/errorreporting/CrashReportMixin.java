package io.github.racoondog.legacyapi.mixin.errorreporting;

import io.github.racoondog.legacyapi.utils.ExceptionUtils;
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
    @Inject(method = "addStackTrace", at = @At("TAIL"))
    private void onAddStackTrace(StringBuilder sb, CallbackInfo info) {
        ExceptionUtils.gatherDebugInfo(sb);
    }
}
