package xyz.pupbrained.drop_confirm.mixin;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.pupbrained.drop_confirm.DropConfirm;
import xyz.pupbrained.drop_confirm.DropConfirmConfig;

@Mixin(Mouse.class)
public abstract class HotbarScrollMixin {
  @Inject(
    method = "onMouseScroll",
    at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/entity/player/PlayerInventory;scrollInHotbar(D)V",
      shift = At.Shift.BEFORE
    )
  )
  private void onHotbarScroll(CallbackInfo ci) {
    if (!AutoConfig.getConfigHolder(DropConfirmConfig.class).getConfig().enabled)
      return;

    DropConfirm.confirmed = false;
  }
}
