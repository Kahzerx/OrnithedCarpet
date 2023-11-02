package com.kahzerx.carpet.mixins.protocol;

import com.kahzerx.carpet.CarpetServer;
import com.kahzerx.carpet.network.CarpetClient;
import net.minecraft.client.Minecraft;
//#if MC>10710
import net.minecraft.client.entity.living.player.LocalClientPlayerEntity;
//#else
//$$ import net.minecraft.client.entity.living.player.LocalClientPlayerEntity;
//#endif
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC>10710
@Mixin(LocalClientPlayerEntity.class)
//#else
//$$ @Mixin(LocalClientPlayerEntity.class)
//#endif
public class LocalClientPlayerEntityMixin {
	//#if MC>10710
	@Shadow
	protected Minecraft minecraft;
	//#endif

	@Inject(method = "sendChat", at = @At(value = "HEAD"))
	private void onSendChat(String string, CallbackInfo ci) {
		if (string.startsWith("/call ")) {
			String command = string.substring(6);
			CarpetClient.sendClientCommand(command);
		}
		//#if MC>10710
		if (CarpetServer.minecraftServer == null && !CarpetClient.isCarpetServer() && minecraft.player != null) {
		//#else
		//$$ if (CarpetServer.minecraftServer == null && !CarpetClient.isCarpetServer() && Minecraft.getInstance().player != null) {
		//#endif
			//#if MC>11202
			CarpetServer.forEachManager(sm -> sm.inspectClientsideCommand(minecraft.player.createCommandSourceStack(), string));
			//#elseif MC>10710
			//$$ CarpetServer.forEachManager(sm -> sm.inspectClientsideCommand(minecraft.player, string));
			//#else
			//$$ CarpetServer.forEachManager(sm -> sm.inspectClientsideCommand(Minecraft.getInstance().player, string));
			//#endif
		}
	}
}
