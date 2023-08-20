package com.kahzerx.carpet;

import com.kahzerx.carpet.api.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.source.CommandSourceStack;

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

	public static void registerCarpetCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
		if (settingsManager == null) {
			return;
		}
		settingsManager.registerCommand(dispatcher);
	}
}
