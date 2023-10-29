package com.kahzerx.carpet;

import com.kahzerx.carpet.api.settings.SettingsManager;
//#if MC>=11300
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.source.CommandSourceStack;
//#else
//$$ import net.minecraft.server.command.handler.CommandRegistry;
//#endif
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

	//#if MC>=11300
	public static void registerCarpetCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
	//#else
	//$$ public static void registerCarpetCommands(CommandRegistry registry) {
	//#endif
		if (settingsManager == null) {
			return;
		}
		//#if MC>=11300
		settingsManager.registerCommand(dispatcher);
		//#else
		//$$ registry.register(new SettingsManager.CarpetCommand());
		//#endif
	}
}
