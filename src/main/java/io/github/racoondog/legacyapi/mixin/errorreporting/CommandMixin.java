package io.github.racoondog.legacyapi.mixin.errorreporting;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.racoondog.legacyapi.utils.ExceptionUtils;
import meteordevelopment.meteorclient.systems.commands.Command;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.command.CommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(value = Command.class, remap = false)
public abstract class CommandMixin {
    @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lmeteordevelopment/meteorclient/systems/commands/Command;build(Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;)V"))
    private void catchError(Command instance, LiteralArgumentBuilder<CommandSource> commandSourceLiteralArgumentBuilder) {
        try {
            instance.build(commandSourceLiteralArgumentBuilder);
        } catch (NoSuchMethodError e) {
            ExceptionUtils.logException(instance.getClass(), ExceptionUtils.AddonExceptionType.OUTDATED_COMMAND_ARGUMENT_TYPE);
            return;
        }
    }
}
