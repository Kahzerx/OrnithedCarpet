package com.kahzerx.carpet.mixins.rule.creativeNoClip;

import com.kahzerx.carpet.CarpetSettings;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.entity.player.PlayerAbilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#if MC<=10710
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#endif

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
	@Shadow
	public PlayerAbilities abilities;

	//#if MC>10710
	@Redirect(method = {"tick", "tickAI"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/living/player/PlayerEntity;isSpectator()Z"))
	private boolean noClip(PlayerEntity instance) {
		return instance.isSpectator() || (CarpetSettings.creativeNoClip && instance.abilities.invulnerable && instance.abilities.flying);
	}
	//#else
	//$$ @Redirect(method = "tickAI", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/living/player/PlayerEntity;getHealth()F", ordinal = 3))
	//$$ private float noClip(PlayerEntity instance) {
	//$$	if (instance.getHealth() > 0.0F && CarpetSettings.creativeNoClip && instance.abilities.invulnerable && instance.abilities.flying) {
	//$$		return -0.1F;
	//$$	}
	//$$	return instance.getHealth();
	//$$ }
	//#endif
	//#if MC>10809
	@Inject(method = "updatePlayerPose", at = @At(value = "HEAD"), cancellable = true)
	private void noPose(CallbackInfo ci) {
		if (CarpetSettings.creativeNoClip && abilities.invulnerable && abilities.flying) {
			ci.cancel();
		}
	}
	//#endif
}
