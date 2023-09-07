package com.kahzerx.carpet;

import com.kahzerx.carpet.api.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.source.CommandSourceStack;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.ornithemc.osl.lifecycle.api.server.MinecraftServerEvents;
import net.ornithemc.osl.networking.api.server.ServerConnectionEvents;

import java.util.ArrayList;
import java.util.List;

public class CarpetServer {
	public static MinecraftServer minecraftServer;
	public static SettingsManager settingsManager;
	public static final List<CarpetExtension> extensions = new ArrayList<>();

	public static void onGameStarted() {
		MinecraftServerEvents.START.register(server -> {
			CarpetServer.minecraftServer = server;
		});
		MinecraftServerEvents.LOAD_WORLD.register(server -> {
			CarpetServer.onServerLoaded();
		});
		MinecraftServerEvents.READY_WORLD.register(server -> {
			CarpetServer.onServerLoadedWorlds();
		});
		MinecraftServerEvents.TICK_START.register(server -> {
			CarpetServer.onServerTick();
		});
		MinecraftServerEvents.STOP.register(server -> {
			CarpetServer.onServerClosed();
			CarpetServer.minecraftServer = null;
		});
		ServerConnectionEvents.LOGIN.register((server, player) -> {
			CarpetServer.onPlayerLoggedIn(player);
		});
		ServerConnectionEvents.DISCONNECT.register((server, player) -> {
			CarpetServer.onPlayerLoggedOut(player);
		});

		settingsManager = new SettingsManager(CarpetSettings.carpetVersion, "carpet", "Ornithe Carpet");
		settingsManager.parseSettingsClass(CarpetSettings.class);
		extensions.forEach(CarpetExtension::onGameStarted);
	}

	public static void onServerLoaded() {
		extensions.forEach(extension -> extension.onServerLoaded(minecraftServer));
	}

	public static void onServerLoadedWorlds() {
		extensions.forEach(extension -> extension.onServerLoadedWorlds(minecraftServer));
	}

	public static void onServerTick() {
		extensions.forEach(extension -> extension.onTick(minecraftServer));
	}

	public static void onServerClosed() {
		extensions.forEach(extension -> extension.onServerClosed(minecraftServer));
	}

	public static void onPlayerLoggedIn(ServerPlayerEntity player) {
		extensions.forEach(extension -> extension.onPlayerLoggedIn(player));
	}

	public static void onPlayerLoggedOut(ServerPlayerEntity player) {
		extensions.forEach(extension -> extension.onPlayerLoggedOut(player));
	}

	public static void registerCarpetCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
		if (settingsManager == null) {
			return;
		}
		settingsManager.registerCommand(dispatcher);
	}
}
