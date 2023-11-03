package com.kahzerx.carpet.mixins.rule.tnt.tntDoNotUpdate;

import com.kahzerx.carpet.CarpetSettings;
import net.minecraft.block.TntBlock;
//#if MC>10710
import net.minecraft.util.math.BlockPos;
//#endif
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TntBlock.class)
public class TntBlockMixin {
	@Redirect(
		method = "onAdded",
		at = @At(
			value = "INVOKE",
			//#if MC>10710
			target = "Lnet/minecraft/world/World;hasNeighborSignal(Lnet/minecraft/util/math/BlockPos;)Z"
			//#else
			//$$ target = "Lnet/minecraft/world/World;hasNeighborSignal(III)Z"
			//#endif
		)
	)
	//#if MC>10710
	private boolean onTNTPlaced(World instance, BlockPos pos) {
	//#else
	//$$ private boolean onTNTPlaced(World instance, int x, int y, int z) {
	//#endif
		if (CarpetSettings.tntDoNotUpdate) {
			return false;
		}
		//#if MC>10710
		return instance.hasNeighborSignal(pos);
		//#else
		//$$ return instance.hasNeighborSignal(x, y, z);
		//#endif
	}
}
