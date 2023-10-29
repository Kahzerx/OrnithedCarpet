package com.kahzerx.carpet.utils;

import com.kahzerx.carpet.CarpetSettings;
import net.minecraft.server.MinecraftServer;
//#if MC>=11300
import net.minecraft.server.command.source.CommandSourceStack;
//#else
//$$ import net.minecraft.server.command.source.CommandSource;
//#endif
import net.minecraft.server.entity.living.player.ServerPlayerEntity;

public class CommandHelper {
	//#if MC>=11300
	public static boolean canUseCommand(CommandSourceStack source, Object commandLevel) {
	//#else
	//$$ public static boolean canUseCommand(CommandSource source, Object commandLevel) {
	//#endif
		if (commandLevel instanceof Boolean) {
			return (Boolean) commandLevel;
		}
		switch (commandLevel.toString()) {
			case "true":
				return true;
			case "false":
				return false;
			case "ops":
				//#if MC>=11300
				return source.hasPermissions(2);
				//#else
				//$$ return source.canUseCommand(2, source.getName());
				//#endif
			case "0":
			case "1":
			case "2":
			case "3":
			case "4":
				//#if MC>=11300
				return source.hasPermissions(Integer.parseInt(commandLevel.toString()));
				//#else
				//$$ return source.canUseCommand(Integer.parseInt(commandLevel.toString()), source.getName());
				//#endif
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
					//#if MC>=11300
					server.getCommandHandler().sendCommands(player);
					//#endif
				}
			} catch (NullPointerException e) {
				CarpetSettings.LOG.warn("Exception while refreshing commands, please report this to Carpet", e);
			}
		});
	}
}
