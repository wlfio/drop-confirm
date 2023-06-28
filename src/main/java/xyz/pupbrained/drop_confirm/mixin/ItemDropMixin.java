package xyz.pupbrained.drop_confirm.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.pupbrained.drop_confirm.DropConfirm;
import xyz.pupbrained.drop_confirm.config.DropConfirmConfig;

import java.util.Objects;

@Mixin(ClientPlayerEntity.class)
public abstract class ItemDropMixin {
  private boolean isDropConfirmDisabled() {
    return !DropConfirmConfig.INSTANCE.getConfig().enabled;
  }

  private boolean isMainHandStackEmpty() {
    var player = Objects.requireNonNull(MinecraftClient.getInstance().player);
    return player.getInventory().getMainHandStack().isEmpty();
  }

  @Inject(method = "dropSelectedItem", at = @At("HEAD"), cancellable = true)
  public void onItemDrop(boolean entireStack, CallbackInfoReturnable<Boolean> cir) {
    // If the mod is disabled or there's nothing
    // in the current slot, don't do anything.
    if (isDropConfirmDisabled() || isMainHandStackEmpty())
      return;

    final var config = DropConfirmConfig.INSTANCE.getConfig();
    final var mc = MinecraftClient.getInstance();
    final var player = Objects.requireNonNull(mc.player);
    final var action = entireStack
      ? PlayerActionC2SPacket.Action.DROP_ALL_ITEMS
      : PlayerActionC2SPacket.Action.DROP_ITEM;
    final var inventory = player.getInventory();
    var itemStack = inventory.getMainHandStack();

    if (!DropConfirm.confirmed) {
      mc.inGameHud.setOverlayMessage(
        Text.of(
          String.format("Press %s again to drop this item.",
            mc
              .options
              .dropKey
              .getKeyName()
              .getString()
          )
        ), false);
      DropConfirm.confirmed = true;
      new Thread(() -> {
        try {
          Thread.sleep((long) (config.confirmationResetDelay * 1000));
          synchronized (DropConfirm.class) {
            DropConfirm.confirmed = false;
          }
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          DropConfirm.LOGGER.error("Interrupted while waiting to reset confirmation.", e);
        }
      }).start();
    } else {
      DropConfirm.confirmed = false;
      itemStack = inventory.dropSelectedItem(entireStack);

      mc.inGameHud.setOverlayMessage(Text.empty(), false);

      if (config.playSounds)
        player.playSound(SoundEvents.ITEM_BUNDLE_DROP_CONTENTS, 1.0F, 1.0F);

      player.networkHandler.sendPacket(new PlayerActionC2SPacket(action, BlockPos.ORIGIN, Direction.DOWN));
    }

    cir.setReturnValue(!itemStack.isEmpty());
  }
}
