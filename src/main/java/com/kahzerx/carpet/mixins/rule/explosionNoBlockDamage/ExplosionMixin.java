package com.kahzerx.carpet.mixins.rule.explosionNoBlockDamage;

import com.kahzerx.carpet.CarpetSettings;
//#if MC>10710
import net.minecraft.block.state.BlockState;
//#endif
//#if MC<=10809
//$$ import net.minecraft.block.Block;
//#endif
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
//#if MC>11202
import net.minecraft.world.BlockView;
//#else
//$$ import net.minecraft.world.World;
//#endif
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
//#if MC<=11202
//$$ import net.minecraft.block.material.Material;
//#endif

@Mixin(Explosion.class)
public class ExplosionMixin {
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

	@Redirect(
		method = "damageEntities",
		at = @At(
			value = "INVOKE",
			//#if MC>11202
			target = "Lnet/minecraft/entity/Entity;canExplodeBlock(Lnet/minecraft/world/explosion/Explosion;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/BlockState;F)Z"
			//#elseif MC<=10710
			//$$ target = "Lnet/minecraft/entity/Entity;canExplodeBlock(Lnet/minecraft/world/explosion/Explosion;Lnet/minecraft/world/World;IIILnet/minecraft/block/Block;F)Z"
			//#elseif MC<=10809
			//$$ target = "Lnet/minecraft/entity/Entity;canExplodeBlock(Lnet/minecraft/world/explosion/Explosion;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/BlockState;F)Z"
			//#else
			//$$ target = "Lnet/minecraft/entity/Entity;canExplodeBlock(Lnet/minecraft/world/explosion/Explosion;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/BlockState;F)Z"
			//#endif
		)
	)
	//#if MC>11202
	private boolean onEnumBlocks(Entity instance, Explosion explosion, BlockView blockView, BlockPos blockPos, BlockState blockState, float v) {
	//#elseif MC<=10710
	//$$ private boolean onEnumBlocks(Entity instance, Explosion explosion, World world, int x, int y, int z, Block block, float v) {
	//#else
	//$$ private boolean onEnumBlocks(Entity instance, Explosion explosion, World world, BlockPos pos, BlockState state, float power) {
	//#endif
		if (CarpetSettings.explosionNoBlockDamage) {
			return false;
		}
		//#if MC>11202
		return instance.canExplodeBlock(explosion, blockView, blockPos, blockState, v);
		//#elseif MC<=10710
		//$$ return instance.canExplodeBlock(explosion, world, x, y, z, block, v);
		//#else
		//$$ return instance.canExplodeBlock(explosion, world, pos, state, power);
		//#endif
	}
}
