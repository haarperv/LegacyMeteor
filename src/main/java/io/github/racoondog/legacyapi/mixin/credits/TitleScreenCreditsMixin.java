package io.github.racoondog.legacyapi.mixin.credits;

import io.github.racoondog.legacyapi.LegacyAPIAddon;
import io.github.racoondog.legacyapi.config.LegacyAPISystem;
import io.github.racoondog.legacyapi.utils.AddonInfo;
import io.github.racoondog.legacyapi.utils.ExceptionUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.addons.AddonManager;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.utils.player.TitleScreenCredits;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Comparator;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Environment(EnvType.CLIENT)
@Mixin(value = TitleScreenCredits.class, remap = false)
public abstract class TitleScreenCreditsMixin {
    @Shadow @Final private static int WHITE;
    @Shadow @Final private static int GRAY;

    @Inject(method = "add", at = @At("HEAD"), cancellable = true)
    private static void customCredits(MeteorAddon addon, CallbackInfo ci) {
        if (addon == LegacyAPIAddon.INSTANCE) ci.cancel();
    }

    @Unique private static final ObjectList<MeteorAddon> addons = new ObjectArrayList<>();
    @Unique private static boolean initialized = false;

    @Unique private static void initialize() {
        initialized = true;
        for (var addon : AddonManager.ADDONS) {
            if (addon == LegacyAPIAddon.INSTANCE) continue;

            StringBuilder sb = new StringBuilder(addon.name).append(" by ");
            for (int i = 0; i < addon.authors.length; i++) {
                if (i > 0) {
                    sb.append(i == addon.authors.length - 1 ? " & " : ", ");
                }

                sb.append(addon.authors[i]);
            }

            if (((AddonInfo) addon).hasRedMark()) {
                sb.append("*");
                ((AddonInfo) addon).setUpdatedWidth(true);
            }

            ((AddonInfo) addon).setWidth(mc.textRenderer.getWidth(sb.toString()));
            addons.add(addon);
        }
        ((AddonInfo) MeteorClient.ADDON).setWidth(mc.textRenderer.getWidth("Meteor Client by MineGame159, squidoodly & seasnail"));
        addons.add(MeteorClient.ADDON);
        addons.sort(Comparator.comparingInt(addon -> -((AddonInfo) addon).getWidth()));
    }
    @Inject(method = "init", at = @At("TAIL"))
    private static void injectInit(CallbackInfo ci) {
        mc.updateWindowTitle();
        if (LegacyAPISystem.get().titleScreenModifications.get()) initialize();
    }
    @Inject(method = "render", at = @At("TAIL"))
    private static void renderCustomCredits(MatrixStack matrices, CallbackInfo ci) {
        assert mc.currentScreen != null;

        int y = 3;

        if (LegacyAPISystem.get().titleScreenModifications.get()) {
            if (!initialized) initialize();
            for (var addon : addons) {
                if (((AddonInfo) addon).hasRedMark() && !((AddonInfo) addon).hasUpdatedWidth()) {
                    //Update width to add red mark
                    ((AddonInfo) addon).setUpdatedWidth(true);
                    ((AddonInfo) addon).setWidth(((AddonInfo) addon).getWidth() + mc.textRenderer.getWidth("*"));
                }

                if (((AddonInfo) addon).isOutdated()) {
                    int x = mc.currentScreen.width - 3 - ((AddonInfo) addon).getWidth() - mc.textRenderer.getWidth("*");

                    mc.textRenderer.drawWithShadow(matrices, "*", x, y, 43520);
                }

                if (FabricLoader.getInstance().isDevelopmentEnvironment()) mc.textRenderer.drawWithShadow(matrices, addon.name, 3, y, 43520);

                y += mc.textRenderer.fontHeight + 2;
            }
        }

        int width1 = mc.textRenderer.getWidth("Legacy ");
        int width2 = mc.textRenderer.getWidth("A");
        int width3 = mc.textRenderer.getWidth("P");
        int width4 = mc.textRenderer.getWidth("I");
        int width = mc.textRenderer.getWidth(" by ");
        int widthName = mc.textRenderer.getWidth("Crosby");

        int x = mc.currentScreen.width - 3 - width - widthName - width1 - width2 - width3 - width4;
        y = mc.currentScreen.height - 22;

        mc.textRenderer.drawWithShadow(matrices, "Legacy ", x, y, WHITE);
        mc.textRenderer.drawWithShadow(matrices, "A", x + width1, y, Color.fromRGBA(186, 18, 0, 255));
        mc.textRenderer.drawWithShadow(matrices, "P", x + width1 + width2, y, Color.fromRGBA(5, 102, 141, 255));
        mc.textRenderer.drawWithShadow(matrices, "I", x + width1 + width2 + width3, y, Color.fromRGBA(35, 206, 107, 255));
        mc.textRenderer.drawWithShadow(matrices, " by ", x + width1 + width2 + width3 + width4, y, GRAY);
        mc.textRenderer.drawWithShadow(matrices, "Crosby", x + width1 + width2 + width3 + width4 + width, y, WHITE);
    }

    @Unique private static MeteorAddon curr;

    @Redirect(method = "lambda$init$1", at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/addons/MeteorAddon;getRepo()Lmeteordevelopment/meteorclient/addons/GithubRepo;", ordinal = 1))
    private static GithubRepo setCurr(MeteorAddon instance) {
        curr = instance;
        return instance.getRepo();
    }

    @Inject(method = "lambda$init$1", at = @At(value = "INVOKE", target = "Ljava/util/List;add(ILjava/lang/Object;)V"))
    private static void markAsNotLatest(CallbackInfo ci) {
        ((AddonInfo) curr).setRedMark(true);
    }

    @Unique private static boolean rateLimited = false;

    @Redirect(method = "lambda$init$1", at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/addons/MeteorAddon;getCommit()Ljava/lang/String;"))
    private static String catchRateLimit(MeteorAddon instance) {
        if (rateLimited) return null;
        try {
            return instance.getCommit();
        } catch (NullPointerException e) {
            rateLimited = true;
            ExceptionUtils.logGlobalException(ExceptionUtils.GlobalExceptionType.GITHUB_API_RATE_LIMIT);
            return null;
        }
    }
}
