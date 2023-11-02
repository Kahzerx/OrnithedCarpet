package com.kahzerx.carpet.mixins.server;

import com.kahzerx.carpet.CarpetServer;
import com.kahzerx.carpet.fakes.PlayerManagerAllowCommands;
import com.kahzerx.carpet.network.ServerNetworkHandler;
import net.minecraft.network.Connection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin implements PlayerManagerAllowCommands {
	@Shadow
	private boolean allowCommands;

	@Override
	public boolean allowCommands() {
		return allowCommands;
	}

	@Inject(method = "onLogin", at = @At("RETURN"))
	private void onPlayerConnected(Connection connection, ServerPlayerEntity serverPlayerEntity, CallbackInfo ci) {
		CarpetServer.onPlayerLoggedIn(serverPlayerEntity);
	}

	@Inject(method = "sendWorldInfo", at = @At("RETURN"))
	private void onSendInfo(ServerPlayerEntity serverPlayerEntity, ServerWorld serverWorld, CallbackInfo ci) {
		ServerNetworkHandler.sendPlayerWorldData(serverPlayerEntity, serverWorld);
	}
}
