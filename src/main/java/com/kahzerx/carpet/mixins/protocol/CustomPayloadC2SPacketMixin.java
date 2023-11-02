package com.kahzerx.carpet.mixins.protocol;

import com.kahzerx.carpet.fakes.CustomPayloadC2SPacketAccess;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.resource.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CustomPayloadC2SPacket.class)
public class CustomPayloadC2SPacketMixin implements CustomPayloadC2SPacketAccess {
	@Shadow
	//#if MC>11202
	private Identifier channel;
	//#else
	//$$ private String channel;
	//#endif

	@Shadow
	//#if MC>10710
	private PacketByteBuf data;
	//#else
	//$$ private byte[] data;
	//#endif

	@Override
	//#if MC>11202
	public Identifier getChannel() {
	//#else
	//$$ public String getChannel() {
	//#endif
		return channel;
	}

	@Override
	//#if MC>10710
	public PacketByteBuf getData() {
		return new PacketByteBuf(data.copy());
	//#else
	//$$ public byte[] getData() {
	//$$ 	return data;
	//#endif
	}
}
