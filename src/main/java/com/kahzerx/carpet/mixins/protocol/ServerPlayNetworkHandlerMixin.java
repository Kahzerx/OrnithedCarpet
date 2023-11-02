package com.kahzerx.carpet.mixins.protocol;

import com.kahzerx.carpet.fakes.CustomPayloadC2SPacketAccess;
import com.kahzerx.carpet.network.CarpetClient;
import com.kahzerx.carpet.network.ServerNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC<=10809
//$$ import java.io.IOException;
//#endif

//#if MC<=10710
//$$ import net.minecraft.network.PacketByteBuf;
//$$ import io.netty.buffer.Unpooled;
//#endif

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
	@Shadow
	public ServerPlayerEntity player;

	@Inject(method = "handleCustomPayload", at = @At(value = "HEAD"), cancellable = true)
	private void onCustomPayload(CustomPayloadC2SPacket customPayloadC2SPacket, CallbackInfo ci) {
		//#if MC>11202
		if (CarpetClient.CARPET_CHANNEL.equals(((CustomPayloadC2SPacketAccess) customPayloadC2SPacket).getChannel())) {
		//#else
		//$$ if (CarpetClient.CARPET_CHANNEL.toString().equals(((CustomPayloadC2SPacketAccess) customPayloadC2SPacket).getChannel())) {
		//#endif
			//#if MC<=10710
			//$$
			//#elseif MC<=10809
			//$$ try {
			//#endif
				//#if MC>10710
				NbtCompound nbt = ((CustomPayloadC2SPacketAccess) customPayloadC2SPacket).getData().readNbtCompound();
				//#else
				//$$ NbtCompound nbt = new PacketByteBuf(Unpooled.wrappedBuffer(((CustomPayloadC2SPacketAccess) customPayloadC2SPacket).getData())).readNbtCompound();
				//#endif
				if (nbt != null) {
					ServerNetworkHandler.onClientData(player, nbt);
				}
				ci.cancel();
			//#if MC<=10710
			//$$
			//#elseif MC<=10809
			//$$ } catch (IOException ignored) {}
			//#endif
		}
	}
}
