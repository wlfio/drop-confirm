package xyz.pupbrained.drop_confirm.mixin;

import net.minecraft.client.MinecraftClient;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import xyz.pupbrained.drop_confirm.Util;
import xyz.pupbrained.drop_confirm.config.DropConfirmConfig;

@Mixin(MinecraftClient.class)
public abstract class HotbarSwitchMixin {
  private static int lastSlot = 0;

  @Inject(
    method = "handleInputEvents",
    at = @At(
      value = "FIELD",
      target = "Lnet/minecraft/entity/player/PlayerInventory;selectedSlot:I",
      opcode = Opcodes.PUTFIELD,
      shift = At.Shift.BEFORE
    ),
    locals = LocalCapture.CAPTURE_FAILHARD
  )
  private void onHotbarSwitch(CallbackInfo ci, int i) {
    if (!DropConfirmConfig.INSTANCE.getConfig().enabled)
      return;

    if (i != lastSlot) {
      Util.confirmed = false;
      lastSlot = i;
    }
  }
}
