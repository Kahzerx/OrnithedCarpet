package com.kahzerx.carpet.mixins.rule.creativePlayersLoadChunks;

import com.kahzerx.carpet.CarpetSettings;
import com.kahzerx.carpet.fakes.ChunkMapAccess;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
//#if MC>=11000
import net.minecraft.world.GameMode;
//#else
//$$ import net.minecraft.world.WorldSettings.GameMode;
//#endif
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC>=10900
@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
	@Shadow
	public abstract ServerWorld getServerWorld();

	public ServerPlayerEntityMixin(World world, GameProfile profile) {
		super(world, profile);
	}

	@Inject(method = "setGameMode", at = @At("HEAD"))
	private void onGameModeChange(GameMode gameMode, CallbackInfo ci) {
		if(!CarpetSettings.creativePlayersLoadChunks) {
			ChunkMapAccess access = ((ChunkMapAccess)getServerWorld().getChunkMap());

			if(gameMode.isCreative()) {
				access.unloadNearestChunks((ServerPlayerEntity) (Object) this);
			}else if(abilities.creativeMode) {
				access.syncChunks((ServerPlayerEntity) (Object) this);
			}

		}
	}
}
//#else
//$$ @Mixin(ServerPlayerEntity.class)
//$$ public abstract class ServerPlayerEntityMixin {}
//#endif
