package io.github.racoondog.legacyapi.mixin.meteor;

import io.github.racoondog.legacyapi.LegacyAPIAddon;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.utils.player.TitleScreenCredits;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

    @Inject(method = "render", at = @At("TAIL"))
    private static void renderCustomCredits(MatrixStack matrices, CallbackInfo ci) {
        //temp solution until im able to access Credit and Section
        int width1 = mc.textRenderer.getWidth("Legacy ");
        int width2 = mc.textRenderer.getWidth("A");
        int width3 = mc.textRenderer.getWidth("P");
        int width4 = mc.textRenderer.getWidth("I");
        int width = mc.textRenderer.getWidth(" by ");
        int widthName = mc.textRenderer.getWidth("Crosby");

        assert mc.currentScreen != null;
        int x = mc.currentScreen.width - 3 - width - widthName - width1 - width2 - width3 - width4;
        int y = mc.currentScreen.height - 20;

        mc.textRenderer.drawWithShadow(matrices, "Legacy ", x, y, WHITE);
        mc.textRenderer.drawWithShadow(matrices, "A", x + width1, y, Color.fromRGBA(186, 18, 0, 255));
        mc.textRenderer.drawWithShadow(matrices, "P", x + width1 + width2, y, Color.fromRGBA(5, 102, 141, 255));
        mc.textRenderer.drawWithShadow(matrices, "I", x + width1 + width2 + width3, y, Color.fromRGBA(35, 206, 107, 255));
        mc.textRenderer.drawWithShadow(matrices, " by ", x + width1 + width2 + width3 + width4, y, GRAY);
        mc.textRenderer.drawWithShadow(matrices, "Crosby", x + width1 + width2 + width3 + width4 + width, y, WHITE);
    }
}
