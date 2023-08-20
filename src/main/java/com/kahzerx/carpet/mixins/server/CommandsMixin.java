package com.kahzerx.carpet.mixins.server;

import com.kahzerx.carpet.CarpetServer;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.handler.CommandManager;
import net.minecraft.server.command.source.CommandSourceStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandManager.class)
public class CommandsMixin {
	@Shadow
	@Final
	private CommandDispatcher<CommandSourceStack> dispatcher;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void onRegister(boolean isDedicatedServer, CallbackInfo ci) {
		CarpetServer.registerCarpetCommands(this.dispatcher);
	}
}
