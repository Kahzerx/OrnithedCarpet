package com.kahzerx.carpet.fakes;

import net.minecraft.server.entity.living.player.ServerPlayerEntity;

public interface ChunkMapAccess {
	void syncChunks(ServerPlayerEntity player);
	void unloadNearestChunks(ServerPlayerEntity player);
}
