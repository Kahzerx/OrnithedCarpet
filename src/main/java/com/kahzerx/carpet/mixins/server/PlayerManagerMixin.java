package com.kahzerx.carpet.mixins.server;

import com.kahzerx.carpet.fakes.PlayerManagerAllowCommands;
import net.minecraft.server.PlayerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin implements PlayerManagerAllowCommands {
	@Shadow
	private boolean allowCommands;

	@Override
	public boolean allowCommands() {
		return allowCommands;
	}
}
