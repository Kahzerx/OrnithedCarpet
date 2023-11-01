package com.kahzerx.carpet.mixins.server;

import com.kahzerx.carpet.CarpetServer;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
	@Inject(method = "stop", at = @At(value = "HEAD"))
	private void onServerClosed(CallbackInfo ci) {
		CarpetServer.onServerClosed((MinecraftServer) (Object) this);
	}

	@Inject(method = "stop", at = @At(value = "TAIL"))
	private void onServerDoneClosing(CallbackInfo ci) {
		CarpetServer.onServerDoneClosing((MinecraftServer) (Object) this);
	}
}
