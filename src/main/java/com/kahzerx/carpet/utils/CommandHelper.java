package com.kahzerx.carpet.utils;

import com.kahzerx.carpet.CarpetSettings;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.source.CommandSourceStack;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;

public class CommandHelper {
	public static boolean canUseCommand(CommandSourceStack source, Object commandLevel) {
		if (commandLevel instanceof Boolean) {
			return (Boolean) commandLevel;
		}
		switch (commandLevel.toString()) {
			case "true":
				return true;
			case "false":
				return false;
			case "ops":
				return source.hasPermissions(2);
			case "0":
			case "1":
			case "2":
			case "3":
			case "4":
				return source.hasPermissions(Integer.parseInt(commandLevel.toString()));
			default:
				return false;
		}
	}

	public static void notifyPlayersCommandsChanged(MinecraftServer server) {
		if (server == null || server.getPlayerManager().getAll() == null) {
			return;
		}
		server.submit(() -> {
			try {
				for (ServerPlayerEntity player : server.getPlayerManager().getAll()) {
					server.getCommandHandler().sendCommands(player);
				}
			} catch (NullPointerException e) {
				CarpetSettings.LOG.warn("Exception while refreshing commands, please report this to Carpet", e);
			}
		});
	}
}
