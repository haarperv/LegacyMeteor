package io.github.racoondog.legacyapi.config;

import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.Settings;
import meteordevelopment.meteorclient.systems.System;
import meteordevelopment.meteorclient.systems.Systems;
import net.minecraft.nbt.NbtCompound;

public class LegacyAPISystem extends System<LegacyAPISystem> {
    public final Settings settings = new Settings();

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    public final Setting<Boolean> preventWindowRename = sgGeneral.add(new BoolSetting.Builder()
        .name("prevent-window-rename")
        .description("Prevents the Minecraft window's title from being modified.")
        .defaultValue(true)
        .build()
    );

    public final Setting<Boolean> preventWindowIconChange = sgGeneral.add(new BoolSetting.Builder()
        .name("prevent-window-icon-change")
        .description("Prevents the Minecraft window's icon from being changed.")
        .defaultValue(true)
        .build()
    );

    public LegacyAPISystem() {
        super("legacy-api");
    }

    public static LegacyAPISystem get() {
        return Systems.get(LegacyAPISystem.class);
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
