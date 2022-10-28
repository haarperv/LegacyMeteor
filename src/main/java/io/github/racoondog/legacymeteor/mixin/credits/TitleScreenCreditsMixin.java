package io.github.racoondog.legacymeteor.mixin.credits;

import io.github.racoondog.legacymeteor.config.LegacyMeteorSystem;
import io.github.racoondog.legacymeteor.utils.AddonInfo;
import io.github.racoondog.legacymeteor.utils.ExceptionUtils;
import io.github.racoondog.meteorsharedaddonutils.features.TitleScreenCredits;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(value = TitleScreenCredits.class, remap = false)
public abstract class TitleScreenCreditsMixin {
    @Shadow @Final private static List<TitleScreenCredits.Credit> credits;

    @Inject(method = "render", at = @At("TAIL"))
    private static void injectRender(MatrixStack matrixStack, CallbackInfo ci) {
        if (LegacyMeteorSystem.get().titleScreenModifications.get()) {
            for (var credit : credits) {
                if (((AddonInfo) credit.addon).isOutdated() && !((AddonInfo) credit.addon).hasUpdatedWidth()) {
                    credit.sections.add(0, new TitleScreenCredits.Section("*", 43520));
                    ((AddonInfo) credit.addon).setUpdatedWidth(true);
                }
            }
        }
    }

    @Redirect(method = "lambda$init$3", at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/addons/MeteorAddon;getCommit()Ljava/lang/String;"))
    private static String catchRateLimit(MeteorAddon instance) {
        try {
            return instance.getCommit();
        } catch (NullPointerException e) {
            ExceptionUtils.logGlobalException(ExceptionUtils.GlobalExceptionType.GITHUB_API_RATE_LIMIT);
            return null;
        }
    }
}
