package com.kahzerx.carpet.mixins.rule.persistentParrots;

import com.kahzerx.carpet.CarpetSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	@Shadow
	protected abstract void dropShoulderEntities();

	protected PlayerEntityMixin(EntityType<?> entityType, World world) {
		super(entityType, world);
	}

	@Redirect(method = "tickAI", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/living/player/PlayerEntity;dropShoulderEntities()V"))
	private void onTickMovement(PlayerEntity instance) {
		if (!CarpetSettings.persistentParrots) {
			this.dropShoulderEntities();
		}
	}

	@Redirect(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/living/player/PlayerEntity;dropShoulderEntities()V"))
	private void onDamage(PlayerEntity instance, DamageSource source, float amount) {
		if (!CarpetSettings.persistentParrots) {
			if (this.random.nextFloat() < (amount / 15.0)) {
				this.dropShoulderEntities();
			}
		}
	}
}
