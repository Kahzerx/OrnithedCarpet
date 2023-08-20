package com.kahzerx.carpet;

import com.kahzerx.carpet.api.settings.SettingsManager;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;

public class CarpetServer {
	public static MinecraftServer minecraftServer;
	public static SettingsManager settingsManager;
	public static final List<CarpetExtension> extensions = new ArrayList<>();
	public static void onGameStarted() {
		settingsManager = new SettingsManager(CarpetSettings.carpetVersion, "carpet", "Ornithe Carpet");
		settingsManager.parseSettingsClass(CarpetSettings.class);
		extensions.forEach(CarpetExtension::onGameStarted);
	}
}
