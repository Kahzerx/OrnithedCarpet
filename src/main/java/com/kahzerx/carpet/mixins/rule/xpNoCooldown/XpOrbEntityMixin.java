package com.kahzerx.carpet.mixins.rule.xpNoCooldown;

import com.kahzerx.carpet.CarpetSettings;
import net.minecraft.entity.XpOrbEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(XpOrbEntity.class)
public class XpOrbEntityMixin {
	@Redirect(method = "onPlayerCollision", at = @At(value = "HEAD"))
	private void removeDelay(PlayerEntity playerEntity) {
		if (CarpetSettings.xpNoCooldown) {
			playerEntity.xpCooldown = 0;
		}
	}
}
