package com.kahzerx.carpet.mixins.rule.tnt.explosionNoBlockDamage;

import com.kahzerx.carpet.CarpetSettings;
//#if MC>10710
import net.minecraft.block.state.BlockState;
//#endif
//#if MC<=10809
//$$ import net.minecraft.block.Block;
//#endif
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
//#if MC<=11202
//$$ import net.minecraft.block.material.Material;
//#endif

@Mixin(Explosion.class)
public abstract class ExplosionMixin {
	@Shadow
	@Final
	private List<BlockPos> damagedBlocks;

	@Redirect(
		method = "damageBlocks",
		at = @At(
			value = "INVOKE",
			//#if MC>11202
			target = "Lnet/minecraft/block/state/BlockState;isAir()Z",
			//#elseif MC<=10809
			//$$ target = "Lnet/minecraft/block/Block;getMaterial()Lnet/minecraft/block/material/Material;",
			//#else
			//$$ target = "Lnet/minecraft/block/state/BlockState;getMaterial()Lnet/minecraft/block/material/Material;",
			//#endif
			ordinal = 0
		)
	)
	//#if MC>11202
	private boolean onDamageBlocks(BlockState instance) {
	//#elseif MC<=10809
	//$$ private Material onDamageBlocks(Block instance) {
	//#else
	//$$ private Material onDamageBlocks(BlockState instance) {
	//#endif
		if (CarpetSettings.explosionNoBlockDamage) {
			//#if MC>11202
			return true;
			//#else
			//$$ return Material.AIR;
			//#endif
		}
		//#if MC>11202
		return instance.isAir();
		//#else
		//$$ return instance.getMaterial();
		//#endif
	}

	@Inject(
		method = "damageEntities",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/List;addAll(Ljava/util/Collection;)Z",
			shift = At.Shift.AFTER
		)
	)
	private void onEnumBlocks(CallbackInfo ci) {
		if (CarpetSettings.explosionNoBlockDamage) {
			damagedBlocks.clear();
		}
	}
}
