package com.kahzerx.carpet.mixins.protocol;

import com.kahzerx.carpet.network.CarpetClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	//#if MC>11202
	@Inject(method = "setWorld(Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/client/gui/screen/Screen;)V", at = @At(value = "HEAD"))
	private void onCloseGame(ClientWorld clientWorld, Screen screen, CallbackInfo ci) {
	//#else
	//$$ @Inject(method = "setWorld(Lnet/minecraft/client/world/ClientWorld;Ljava/lang/String;)V", at = @At(value = "HEAD"))
	//$$ private void onCloseGame(ClientWorld clientWorld, String string, CallbackInfo ci) {
	//#endif
		if (clientWorld == null) {
			CarpetClient.disconnect();
		}
	}
}
