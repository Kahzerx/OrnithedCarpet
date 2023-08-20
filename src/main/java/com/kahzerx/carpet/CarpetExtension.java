package com.kahzerx.carpet;

import com.kahzerx.carpet.api.settings.SettingsManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;

import java.util.Collections;
import java.util.Map;

public interface CarpetExtension {
	default void onGameStarted() {}
	default void onServerLoaded(MinecraftServer server) {}
	default void onServerLoadedWorlds(MinecraftServer server) {}
	default void onTick(MinecraftServer server) {}
	default void onPlayerLoggedIn(ServerPlayerEntity player) {}
	default void onPlayerLoggedOut(ServerPlayerEntity player) {}
	default void onServerClosed(MinecraftServer server) {}
	default void onReload(MinecraftServer server) {}
	default String version() {
		return null;
	}
	default void registerLoggers() {}
	default Map<String, String> canHasTranslations(String lang) {
		return Collections.emptyMap();
	}
}
