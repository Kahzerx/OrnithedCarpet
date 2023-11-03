package com.kahzerx.carpet.mixins.rule.creativeNoClip;

import com.kahzerx.carpet.CarpetSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.world.WorldRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
	//#if MC>10710
	@Shadow
	@Final
	private Minecraft minecraft;

	@ModifyVariable(method = "setupRender", at = @At(value = "HEAD"), ordinal = 0, argsOnly = true)
	private boolean onNoClip(boolean bl) {
		if (CarpetSettings.creativeNoClip && minecraft.player.abilities.invulnerable && minecraft.player.abilities.flying) {
			return true;
		}
		return bl;
	}
	//#endif
}
