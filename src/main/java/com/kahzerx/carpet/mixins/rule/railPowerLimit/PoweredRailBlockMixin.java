package com.kahzerx.carpet.mixins.rule.railPowerLimit;

import com.kahzerx.carpet.CarpetSettings;
import net.minecraft.block.PoweredRailBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(PoweredRailBlock.class)
public class PoweredRailBlockMixin {
	@ModifyConstant(
		method = "isPoweredByConnectedRails",
		constant = @Constant(
			intValue = 8
		)
	)
	private int powerLimit(int original) {
		return CarpetSettings.railPowerLimit - 1;
	}
}
