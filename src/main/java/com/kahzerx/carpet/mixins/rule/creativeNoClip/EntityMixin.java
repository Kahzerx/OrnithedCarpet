package com.kahzerx.carpet.mixins.rule.creativeNoClip;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
//#if MC<=10710
//$$ import com.kahzerx.carpet.CarpetSettings;
//$$ import net.minecraft.util.math.Box;
//$$ import org.spongepowered.asm.mixin.Final;
//$$ import org.spongepowered.asm.mixin.Shadow;
//$$ import net.minecraft.entity.living.player.PlayerEntity;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#endif

@Mixin(Entity.class)
public abstract class EntityMixin {
	//#if MC<=10710
	//$$ @Shadow
	//$$ public float eyeHeight;
	//
	//$$ @Shadow
	//$$ public float eyeHeightSneakOffset;
	//
	//$$ @Shadow
	//$$ public abstract void setPosition(double x, double y, double z);
	//
	//$$ @Final
	//$$ @Shadow
	//$$ public Box shape;
	//
	//$$ @Inject(method = "push", at = @At(value = "HEAD"), cancellable = true)
	//$$ private void onPush(Entity par1, CallbackInfo ci) {
	//$$	if (((Object) this) instanceof PlayerEntity) {
	//$$		PlayerEntity p = (PlayerEntity) (Object) this;
	//$$		if (CarpetSettings.creativeNoClip && p.abilities.invulnerable && p.abilities.flying) {
	//$$			ci.cancel();
	//$$		}
	//$$	}
	//$$ }
	//
	//$$ @Inject(method = "isInWall", at = @At(value = "HEAD"), cancellable = true)
	//$$ private void inWall(CallbackInfoReturnable<Boolean> cir) {
	//$$	if (((Object) this) instanceof PlayerEntity) {
	//$$		PlayerEntity p = (PlayerEntity) (Object) this;
	//$$		if (CarpetSettings.creativeNoClip && p.abilities.invulnerable && p.abilities.flying) {
	//$$			cir.setReturnValue(false);
	//$$		}
	//$$ 	}
	//$$ }
	//
	//$$ @Inject(method = "move", at = @At(value = "HEAD"), cancellable = true)
	//$$ private void onMove(double x, double y, double z, CallbackInfo ci) {
	//$$	if (((Object) this) instanceof PlayerEntity) {
	//$$		PlayerEntity p = (PlayerEntity) (Object) this;
	//$$		if (CarpetSettings.creativeNoClip && p.abilities.invulnerable && p.abilities.flying) {
	//$$			shape.setMoved(x, y, z);
	//$$			setPosition((this.shape.minX + this.shape.maxX) / 2.0, this.shape.minY + (double)eyeHeight - (double)this.eyeHeightSneakOffset, (this.shape.minZ + this.shape.maxZ) / 2.0);
	//$$			ci.cancel();
	//$$		}
	//$$	}
	//$$ }
	//
	//$$ @Inject(method = "canMove", at = @At(value = "HEAD"), cancellable = true)
	//$$ private void onTryMove(double dy, double dz, double par3, CallbackInfoReturnable<Boolean> cir) {
	//$$	if (((Object) this) instanceof PlayerEntity) {
	//$$		PlayerEntity p = (PlayerEntity) (Object) this;
	//$$		if (CarpetSettings.creativeNoClip && p.abilities.invulnerable && p.abilities.flying) {
	//$$			cir.setReturnValue(true);
	//$$		}
	//$$	}
	//$$ }
	//$$
	//#endif
}
