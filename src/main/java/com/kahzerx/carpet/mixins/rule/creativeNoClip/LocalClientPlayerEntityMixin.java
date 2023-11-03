package com.kahzerx.carpet.mixins.rule.creativeNoClip;

import net.minecraft.client.entity.living.player.LocalClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
//#if MC<=10710
//$$ import com.kahzerx.carpet.CarpetSettings;
//$$ import net.minecraft.client.Minecraft;
//$$ import org.spongepowered.asm.mixin.Shadow;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#endif

@Mixin(LocalClientPlayerEntity.class)
public class LocalClientPlayerEntityMixin {
	//#if MC<=10710
	//$$ @Shadow
	//$$ protected Minecraft minecraft;
	//
	//$$ @Inject(method = "pushAwayFrom", at = @At(value = "HEAD"), cancellable = true)
	//$$ private void onCollision(double y, double z, double par3, CallbackInfoReturnable<Boolean> cir) {
	//$$ 	if (CarpetSettings.creativeNoClip && minecraft.player.abilities.invulnerable && minecraft.player.abilities.flying) {
	//$$ 		cir.setReturnValue(false);
	//$$ 	}
	//$$ }
	//#endif
}
