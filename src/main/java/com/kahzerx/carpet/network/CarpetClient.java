package com.kahzerx.carpet.network;

import com.kahzerx.carpet.CarpetServer;
import com.kahzerx.carpet.CarpetSettings;
//#if MC>10710
import net.minecraft.client.entity.living.player.LocalClientPlayerEntity;
//#else
//$$ import net.minecraft.client.entity.living.player.LocalClientPlayerEntity;
//#endif
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
//#if MC>10710
import net.minecraft.resource.Identifier;
//#endif

public class CarpetClient {
	public static final String HI = "69";
	public static final String HELLO = "420";
	//#if MC>10710
	private static LocalClientPlayerEntity clientPlayer = null;
	//#else
	//$$ private static LocalClientPlayerEntity clientPlayer = null;  // For some reason its translating to InputClientPlayerEntity on build, must be somehow linked in the mappings...
	//#endif
	private static boolean carpetServer = false;
	public static String serverCarpetVersion;
	//#if MC>10710
	public static final Identifier CARPET_CHANNEL = new Identifier("carpet:hello");
	//#else
	//$$ public static final String CARPET_CHANNEL = "carpet:hello";
	//#endif

	//#if MC>10710
	public static void gameJoined(LocalClientPlayerEntity player) {
		clientPlayer = player;
	}
	//#else
	//$$ public static void gameJoined(LocalClientPlayerEntity player) {
	//$$	clientPlayer = player;
	//$$ }
	//#endif

	public static void disconnect() {
		if (carpetServer) {  // multiplayer connection
			carpetServer = false;
			clientPlayer = null;
			CarpetServer.onServerClosed(null);
			CarpetServer.onServerDoneClosing(null);
		} else {  // singleplayer disconnect
			CarpetServer.clientPreClosing();
		}
	}

	public static void setCarpetServer() {
		CarpetClient.carpetServer = true;
	}

	//#if MC>10710
	public static LocalClientPlayerEntity getClientPlayer() {
	//#else
	//$$ public static LocalClientPlayerEntity getClientPlayer() {
	//#endif
		return clientPlayer;
	}

	public static boolean isCarpetServer() {
		return carpetServer;
	}

	public static boolean sendClientCommand(String command) {
		if (!carpetServer && CarpetServer.minecraftServer == null) {
			return false;
		}
		ClientNetworkHandler.clientCommand(command);
		return true;
	}

	public static void onClientCommand(NbtElement t) {
		CarpetSettings.LOG.info("Server Response:");
		NbtCompound tag = (NbtCompound) t;
		CarpetSettings.LOG.info(" - id: " + tag.getString("id"));
		if (tag.contains("error")) {
			CarpetSettings.LOG.warn(" - error: " + tag.getString("error"));
		}
		if (tag.contains("output")) {
			NbtList outputTag = (NbtList) tag.get("output");
			for (int i = 0; i < outputTag.size(); i++) {
				CarpetSettings.LOG.info(" - response: " + outputTag.getString(i));
			}
		}
	}
}
