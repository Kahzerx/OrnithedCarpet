package com.kahzerx.carpet.fakes;

import net.minecraft.server.entity.living.player.ServerPlayerEntity;

public interface ChunkHolderAccess {
	//Removes player from the chunk holder without sending forget chunk packets
	void removePlayerQuietly(ServerPlayerEntity player);
}
