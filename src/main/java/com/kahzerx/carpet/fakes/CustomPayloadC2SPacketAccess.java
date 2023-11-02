package com.kahzerx.carpet.fakes;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.Identifier;

public interface CustomPayloadC2SPacketAccess {
	//#if MC>11202
	Identifier getChannel();
	//#else
	//$$ String getChannel();
	//#endif

	//#if MC>10710
	PacketByteBuf getData();
	//#else
	//$$ byte[] getData();
	//#endif
}
