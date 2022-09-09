package io.github.racoondog.legacyapi.config;

import io.github.racoondog.legacyapi.utils.ExceptionUtils;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.Settings;
import meteordevelopment.meteorclient.systems.System;
import meteordevelopment.meteorclient.systems.Systems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.MacWindowUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Environment(EnvType.CLIENT)
public class LegacyAPISystem extends System<LegacyAPISystem> {
    public final Settings settings = new Settings();

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    public final Setting<Boolean> preventWindowRename = sgGeneral.add(new BoolSetting.Builder()
        .name("prevent-window-rename")
        .description("Prevents the Minecraft window's title from being modified.")
        .defaultValue(true)
        .onChanged(value -> mc.updateWindowTitle())
        .build()
    );

    public final Setting<Boolean> preventWindowIconChange = sgGeneral.add(new BoolSetting.Builder()
        .name("prevent-window-icon-change")
        .description("Prevents the Minecraft window's icon from being changed.")
        .defaultValue(true)
        .onChanged(value -> {
            if (value) updateIcon();
        })
        .build()
    );

    public final Setting<Boolean> titleScreenModifications = sgGeneral.add(new BoolSetting.Builder()
        .name("title-screen-modifications")
        .description("Enables the title screen modifications.")
        .defaultValue(true)
        .build()
    );

    public final Setting<Boolean> enableLegacyGuiSystem = sgGeneral.add(new BoolSetting.Builder()
        .name("enable-legacy-gui-system")
        .description("Allows using addons which use the old GUI system.")
        .defaultValue(false)
        .build()
    );

    public LegacyAPISystem() {
        super("legacy-api");
        if (preventWindowRename.get()) mc.updateWindowTitle();
        if (preventWindowIconChange.get()) updateIcon();
    }

    public static LegacyAPISystem get() {
        return Systems.get(LegacyAPISystem.class);
    }

    private void updateIcon() {
        try {
            if (MinecraftClient.IS_SYSTEM_MAC) {
                InputStream inputStream = mc.getResourcePackProvider().getPack().open(ResourceType.CLIENT_RESOURCES, new Identifier("icons/minecraft.icns"));
                MacWindowUtil.setApplicationIconImage(inputStream);
            } else {
                InputStream inputStream = mc.getResourcePackProvider().getPack().open(ResourceType.CLIENT_RESOURCES, new Identifier("icons/icon_16x16.png"));
                InputStream inputStream2 = mc.getResourcePackProvider().getPack().open(ResourceType.CLIENT_RESOURCES, new Identifier("icons/icon_32x32.png"));
                mc.getWindow().setIcon(inputStream, inputStream2);
            }
        } catch (IOException e) {
            ExceptionUtils.logGlobalException(ExceptionUtils.GlobalExceptionType.WINDOW_ICON_ERROR);
        }
    }

    @Override
    public NbtCompound toTag() {
        NbtCompound tag = new NbtCompound();

        tag.put("settings", settings.toTag());

        return tag;
    }

    @Override
    public LegacyAPISystem fromTag(NbtCompound tag) {
        if (tag.contains("settings")) settings.fromTag(tag.getCompound("settings"));

        return this;
    }
}
