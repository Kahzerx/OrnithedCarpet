package com.kahzerx.carpet.mixins.rule.creativeNoClip;

import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

//#if MC<=10710
//$$ import com.kahzerx.carpet.CarpetSettings;
//$$ import net.minecraft.entity.living.player.PlayerEntity;
//$$ import java.util.Collections;
//$$ import net.minecraft.entity.Entity;
//$$ import net.minecraft.util.math.Box;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import java.util.List;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#endif

@Mixin(World.class)
public class WorldMixin {
	//#if MC<=10710
	//$$ @Inject(method = "getCollisions", at = @At(value = "HEAD"), cancellable = true)
	//$$ private void onGetCollisions(Entity shape, Box par2, CallbackInfoReturnable<List> cir) {
	//$$	if (shape instanceof PlayerEntity) {
	//$$		PlayerEntity p = (PlayerEntity) shape;
	//$$		if (CarpetSettings.creativeNoClip && p.abilities.invulnerable && p.abilities.flying) {
	//$$			cir.setReturnValue(Collections.emptyList());
	//$$		}
	//$$	}
	//$$ }
	//#endif
}
