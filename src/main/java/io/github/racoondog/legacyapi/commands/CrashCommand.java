package io.github.racoondog.legacyapi.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.systems.commands.Command;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.command.CommandSource;

/**
 * Development environment command to test crash report
 * @author Crosby
 */
@Environment(EnvType.CLIENT)
public class CrashCommand extends Command {
    public CrashCommand() {
        super("legacyapicrash", "Causes the client to crash");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            boolean bool = (Boolean) null;
            return 0;
        });
    }
}
