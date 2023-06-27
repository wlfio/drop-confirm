package xyz.pupbrained.drop_confirm.mixin;

import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.pupbrained.drop_confirm.DropConfirm;

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
    DropConfirm.confirmed = false;
  }
}
