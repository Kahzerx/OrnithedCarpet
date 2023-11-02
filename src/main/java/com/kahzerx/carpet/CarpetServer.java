package com.kahzerx.carpet;

import com.kahzerx.carpet.api.settings.SettingsManager;
import com.kahzerx.carpet.network.ServerNetworkHandler;
//#if MC>=11300
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.source.CommandSourceStack;
//#else
//$$ import net.minecraft.server.command.handler.CommandRegistry;
//#endif
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
		//$$ registry.register(new SettingsManager.CarpetCommand(settingsManager));
		//#endif
	}

	public static void onPlayerLoggedIn(ServerPlayerEntity player) {
		ServerNetworkHandler.onPlayerJoin(player);
		extensions.forEach(e -> e.onPlayerLoggedIn(player));
	}

	public static void onPlayerLoggedOut(ServerPlayerEntity player) {
		ServerNetworkHandler.onPlayerLoggedOut(player);
		extensions.forEach(e -> e.onPlayerLoggedOut(player));
	}

	public static void forEachManager(Consumer<SettingsManager> consumer) {
		consumer.accept(settingsManager);
		for (CarpetExtension e : extensions) {
			SettingsManager manager = e.extensionSettingsManager();
			if (manager != null) {
				consumer.accept(manager);
			}
		}
	}

	public static void onServerClosed(MinecraftServer server) {
		if (minecraftServer != null) {
			ServerNetworkHandler.close();
			extensions.forEach(e -> e.onServerClosed(server));
			minecraftServer = null;
		}
	}

	public static void onServerDoneClosing(MinecraftServer server) {
		forEachManager(SettingsManager::detachServer);
	}

	public static void clientPreClosing() { }
}
