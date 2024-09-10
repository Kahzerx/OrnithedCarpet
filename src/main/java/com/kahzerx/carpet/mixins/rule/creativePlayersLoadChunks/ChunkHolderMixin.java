package com.kahzerx.carpet.mixins.rule.creativePlayersLoadChunks;

import com.kahzerx.carpet.fakes.ChunkHolderAccess;
import net.minecraft.server.ChunkMap;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import java.util.List;

//#if MC>=10900
import net.minecraft.server.ChunkHolder;

@Mixin(ChunkHolder.class)
public class ChunkHolderMixin implements ChunkHolderAccess {

	@Shadow
	@Final
	private List<ServerPlayerEntity> players;

	@Shadow
	@Final
	private ChunkMap chunkMap;

	@Override
	public void removePlayerQuietly(ServerPlayerEntity player) {
		if (this.players.contains(player)) {
			this.players.remove(player);
			if (this.players.isEmpty()) {
				this.chunkMap.unload((ChunkHolder) (Object) this);
			}
		}
	}
}
//#else
//$$ @Mixin(ChunkMap.class)
//$$ public abstract class ChunkHolderMixin {}
//#endif
