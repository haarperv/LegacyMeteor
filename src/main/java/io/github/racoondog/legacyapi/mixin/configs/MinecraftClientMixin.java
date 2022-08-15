package io.github.racoondog.legacyapi.mixin.configs;

import io.github.racoondog.legacyapi.config.LegacyAPISystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.server.integrated.IntegratedServer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(value = MinecraftClient.class, priority = 500)
public abstract class MinecraftClientMixin {
    @Shadow public abstract @Nullable ClientPlayNetworkHandler getNetworkHandler();
    @Shadow private @Nullable IntegratedServer server;
    @Shadow public abstract boolean isConnectedToRealms();
    @Shadow private @Nullable ServerInfo currentServerEntry;

    @Inject(method = "getWindowTitle", at = @At("HEAD"), cancellable = true)
    private void preventWindowTitleModification(CallbackInfoReturnable<String> cir) {
        if (LegacyAPISystem.get() != null && LegacyAPISystem.get().preventWindowRename.get()) {
            //Default Minecraft implementation
            StringBuilder stringBuilder = new StringBuilder("Minecraft");
            if (MinecraftClient.getModStatus().isModded()) {
                stringBuilder.append("*");
            }
            stringBuilder.append(" ");
            stringBuilder.append(SharedConstants.getGameVersion().getName());
            ClientPlayNetworkHandler clientPlayNetworkHandler = this.getNetworkHandler();
            if (clientPlayNetworkHandler != null && clientPlayNetworkHandler.getConnection().isOpen()) {
                stringBuilder.append(" - ");
                if (this.server != null && !this.server.isRemote()) {
                    stringBuilder.append(I18n.translate("title.singleplayer"));
                } else if (this.isConnectedToRealms()) {
                    stringBuilder.append(I18n.translate("title.multiplayer.realms"));
                } else if (this.server != null || this.currentServerEntry != null && this.currentServerEntry.isLocal()) {
                    stringBuilder.append(I18n.translate("title.multiplayer.lan"));
                } else {
                    stringBuilder.append(I18n.translate("title.multiplayer.other"));
                }
            }
            cir.setReturnValue(stringBuilder.toString());
        }
    }
}
