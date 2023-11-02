package com.kahzerx.carpet.network;

import com.kahzerx.carpet.CarpetServer;
import com.kahzerx.carpet.CarpetSettings;
import com.kahzerx.carpet.api.settings.CarpetRule;
import com.kahzerx.carpet.api.settings.RuleHelper;
import com.kahzerx.carpet.fakes.PlayerManagerAllowCommands;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.OpEntry;
//#if MC>11102
import net.minecraft.server.command.source.CommandSourceStack;
//#elseif MC>10710
//$$ import net.minecraft.server.command.source.CommandSource;
//$$ import net.minecraft.server.command.source.CommandResults;
//#else
//$$ import net.minecraft.server.command.source.CommandSource;
//#endif
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;

public class ServerNetworkHandler {
    private static final Map<ServerPlayerEntity, String> remoteCarpetPlayers = new HashMap<>();
    private static final Set<ServerPlayerEntity> validCarpetPlayers = new HashSet<>();

    private static final Map<String, BiConsumer<ServerPlayerEntity, NbtElement>> dataHandlers = new HashMap<String, BiConsumer<ServerPlayerEntity, NbtElement>>(){{
		put(CarpetClient.HELLO, (p, t) -> onHello(p, t.toString()));
		put("clientCommand", (p, t) -> handleClientCommand(p, (NbtCompound) t));
	}};

    public static void onPlayerJoin(ServerPlayerEntity playerEntity) {
        if (!playerEntity.networkHandler.getConnection().isLocal()) {
            NbtCompound data = new NbtCompound();
            data.putString(CarpetClient.HI, CarpetSettings.carpetVersion);
			PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
			buf.writeNbtCompound(data);
            playerEntity.networkHandler.sendPacket(new CustomPayloadS2CPacket(
				//#if MC>11202
				CarpetClient.CARPET_CHANNEL,
				//#else
				//$$ CarpetClient.CARPET_CHANNEL.toString(),
				//#endif
				buf
			));
        } else {
            validCarpetPlayers.add(playerEntity);
        }
    }

    public static void onHello(ServerPlayerEntity playerEntity, String version) {
        validCarpetPlayers.add(playerEntity);
        remoteCarpetPlayers.put(playerEntity, version);
        if (version.equals(CarpetSettings.carpetVersion)) {
			//#if MC>11202
			CarpetSettings.LOG.info("Player " + playerEntity.getName().getString() + " joined with a matching carpet client");
			//#else
			//$$ CarpetSettings.LOG.info("Player " + playerEntity.getName() + " joined with a matching carpet client");
			//#endif
        } else {
			//#if MC>11202
			CarpetSettings.LOG.warn("Player " + playerEntity.getName().getString() + " joined with another carpet version: " + version);
			//#else
			//$$ CarpetSettings.LOG.warn("Player " + playerEntity.getName() + " joined with another carpet version: " + version);
			//#endif
        }
        DataBuilder data = DataBuilder.create(playerEntity.server); // tickrate related settings are sent on world change
        CarpetServer.forEachManager(sm -> sm.getCarpetRules().forEach(data::withRule));
        playerEntity.networkHandler.sendPacket(data.build());
    }

    public static void sendPlayerWorldData(ServerPlayerEntity player, World world) {}

	//#if MC>10710
	private static int getPermissions(MinecraftServer server, GameProfile gameProfile) {
		if (server.getPlayerManager().isOp(gameProfile)) {
			OpEntry opEntry = server.getPlayerManager().getOps().get(gameProfile);
			if (opEntry != null) {
				return opEntry.getPermissionLevel();
			}
			if (server.isSingleplayer()) {
				if (server.getUsername().equals(gameProfile.getName())) {
					return 4;
				}
				return ((PlayerManagerAllowCommands) server.getPlayerManager()).allowCommands() ? 4 : 0;
			}
			return server.getOpPermissionLevel();
		}
		return 0;
	}
	//#endif

    private static void handleClientCommand(ServerPlayerEntity player, NbtCompound commandData) {
        String command = commandData.getString("command");
        String id = commandData.getString("id");
        List<Text> output = new ArrayList<>();
		Text[] error = { null };
		//#if MC>10809
        if (player.getServer() == null) {
		//#else
		//$$ if (player.server == null) {
		//#endif
            error[0] = new LiteralText("No Server");
        } else {
			//#if MC>10809
            player.getServer().getCommandHandler().run(
			//#else
			//$$ player.server.getCommandHandler().run(
			//#endif
				//#if MC>11202
				new CommandSourceStack(
					player,
					player.getSourcePos(),
					player.getRotation(),
					player.world instanceof ServerWorld ? (ServerWorld) player.world : null,
					getPermissions(player.getServer(), player.getGameProfile()),
					player.getName().getString(),
					player.getDisplayName(),
					player.getServer(),
					player
				) {
				//#elseif MC>11102
				//$$ new CommandSourceStack(
				//$$	player,
				//$$	player.getSourcePos(),
				//$$	player.getSourceBlockPos(),
				//$$	getPermissions(player.getServer(), player.getGameProfile()),
				//$$	player,
				//$$	player.sendCommandFeedback()
				//$$ ) {
				//#else
				//$$ new CommandSource() {
				//#endif
						//#if MC>11202
						@Override
						public void sendFailure(Text text) {
							error[0] = text;
						}

						@Override
						public void sendSuccess(Text text, boolean bl) {
							output.add(text);
						}
						//#elseif MC>11102
						//$$ @Override
						//$$ public void sendMessage(Text text) {
						//$$ 	output.add(text);
						//$$ }
						//#else
						//$$
						//$$ @Override
						//$$ public String getName() {
						//$$ 	return player.getName();
						//$$ }
						//$$
						//$$ @Override
						//$$ public Text getDisplayName() {
						//$$ 	return player.getDisplayName();
						//$$ }
						//$$
						//$$ @Override
						//$$ public void sendMessage(Text text) {
						//$$ 	output.add(text);
						//$$ }
						//$$
						//$$ @Override
						//$$ public boolean canUseCommand(int i, String string) {
						//$$ 	return player.canUseCommand(i, string);
						//$$ }
						//$$
						//$$ @Override
						//$$ public BlockPos getSourceBlockPos() {
						//$$ 	return player.getSourceBlockPos();
						//$$ }
						//$$
						//#if MC>10710
						//$$ @Override
						//$$ public Vec3d getSourcePos() {
						//$$ 	return player.getSourcePos();
						//$$ }
						//#endif
						//$$
						//$$ @Override
						//$$ public World getSourceWorld() {
						//$$ 	return player.getSourceWorld();
						//$$ }
						//$$
						//#if MC>10710
						//$$ @Nullable
						//$$ @Override
						//$$ public Entity asEntity() {
						//$$ 	return player.asEntity();
						//$$ }
						//$$
						//$$ @Override
						//$$ public boolean sendCommandFeedback() {
						//$$ 	return player.sendCommandFeedback();
						//$$ }
						//$$
						//$$ @Override
						//$$ public void addResult(CommandResults.Type type, int i) {
						//$$	player.addResult(type, i);
						//$$ }
						//#endif
						//$$
						//#if MC>10809
						//$$ @Nullable
						//$$ @Override
						//$$ public MinecraftServer getServer() {
						//$$	return player.getServer();
						//$$ }
						//#endif
						//#endif
					}, command);
        }
		NbtCompound result = new NbtCompound();
        result.putString("id", id);
        if (error[0] != null) {
            result.putString("error", error[0].getContent());
        }
        NbtList outputResult = new NbtList();
        for (Text line : output) {
            outputResult.add(new NbtString(Text.Serializer.toJson(line)));
        }
        if (!output.isEmpty()) {
            result.put("output", outputResult);
        }
        player.networkHandler.sendPacket(DataBuilder.create(player.server).withCustomNbt("clientCommand", result).build());
    }

    public static void onClientData(ServerPlayerEntity player, NbtCompound compound) {
        for (Object k : compound.getKeys()) {
			String key = (String) k;
            if (dataHandlers.containsKey(key)) {
                dataHandlers.get(key).accept(player, compound.get(key));
            } else {
                CarpetSettings.LOG.warn("Unknown carpet client data: " + key);
            }
        }
    }

    public static void updateRuleWithConnectedClients(CarpetRule<?> rule) {
//        if (CarpetSettings.superSecretSetting) {  // TODO
//            return;
//        }
		for (ServerPlayerEntity player : remoteCarpetPlayers.keySet()) {
            player.networkHandler.sendPacket(DataBuilder.create(player.server).withRule(rule).build());
        }
    }

    public static void broadcastCustomCommand(String command, NbtElement data) {
//        if (CarpetSettings.superSecretSetting) {  // TODO
//            return;
//        }
        for (ServerPlayerEntity player : validCarpetPlayers) {
            player.networkHandler.sendPacket(DataBuilder.create(player.server).withCustomNbt(command, data).build());
        }
    }

    public static void sendCustomCommand(ServerPlayerEntity player, String command, NbtElement data) {
        if (isValidCarpetPlayer(player)) {
            player.networkHandler.sendPacket(DataBuilder.create(player.server).withCustomNbt(command, data).build());
        }
    }

    public static void onPlayerLoggedOut(ServerPlayerEntity player) {
        validCarpetPlayers.remove(player);
        if (!player.networkHandler.getConnection().isLocal()) {
            remoteCarpetPlayers.remove(player);
        }
    }

    public static void close() {
        remoteCarpetPlayers.clear();
        validCarpetPlayers.clear();
    }

    public static boolean isValidCarpetPlayer(ServerPlayerEntity player) {
//        if (CarpetSettings.superSecretSetting) {  // TODO
//            return false;
//        }
        return validCarpetPlayers.contains(player);

    }

    public static String getPlayerStatus(ServerPlayerEntity player) {
        if (remoteCarpetPlayers.containsKey(player)) {
            return "carpet " + remoteCarpetPlayers.get(player);
        }
        if (validCarpetPlayers.contains(player)) {
            return "carpet " + CarpetSettings.carpetVersion;
        }
        return "vanilla";
    }

    private static class DataBuilder {
        private NbtCompound tag;
        // unused now, but hey
        private MinecraftServer server;

        private static DataBuilder create(final MinecraftServer server) {
            return new DataBuilder(server);
        }

        private DataBuilder(MinecraftServer server) {
            tag = new NbtCompound();
            this.server = server;
        }

        private DataBuilder withRule(CarpetRule<?> rule) {
			NbtCompound rules = (NbtCompound) tag.get("Rules");
            if (rules == null) {
                rules = new NbtCompound();
                tag.put("Rules", rules);
            }
            String identifier = rule.settingsManager().identifier();
            String key = rule.name();
            while (rules.contains(key)) {
                key = key + "2";
            }
			NbtCompound ruleNBT = new NbtCompound();
            ruleNBT.putString("Value", RuleHelper.toRuleString(rule.value()));
            ruleNBT.putString("Manager", identifier);
            ruleNBT.putString("Rule", rule.name());
            rules.put(key, ruleNBT);
            return this;
        }

        public DataBuilder withCustomNbt(String key, NbtElement value) {
            tag.put(key, value);
            return this;
        }

        private CustomPayloadS2CPacket build() {
			PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
			buf.writeNbtCompound(tag);
            return new CustomPayloadS2CPacket(
				//#if MC>11202
				CarpetClient.CARPET_CHANNEL,
				//#else
				//$$ CarpetClient.CARPET_CHANNEL.toString(),
				//#endif
				buf
			);
        }
    }
}
