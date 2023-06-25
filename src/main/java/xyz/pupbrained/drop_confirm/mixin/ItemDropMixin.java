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

import java.util.Objects;

import static java.lang.String.format;
import static java.lang.Thread.sleep;

@Mixin(ClientPlayerEntity.class)
public abstract class ItemDropMixin {
  private static boolean confirmed = false;

  @Inject(method = "dropSelectedItem", at = @At("HEAD"), cancellable = true)
  public void onItemDrop(boolean entireStack, CallbackInfoReturnable<Boolean> ci) {
    var mc = MinecraftClient.getInstance();
    var action = entireStack ? PlayerActionC2SPacket.Action.DROP_ALL_ITEMS : PlayerActionC2SPacket.Action.DROP_ITEM;
    var itemStack = Objects.requireNonNull(mc.player).getInventory().getMainHandStack();

    if (itemStack.isEmpty()) {
      ci.setReturnValue(false);
      return;
    }

    if (!confirmed) {
      mc.inGameHud.setOverlayMessage(
        Text.of(
          format("Press %s again to drop this item.",
            mc
              .options
              .dropKey
              .getKeyName()
              .getString()
          )
        ), false);
      confirmed = true;
      new Thread(() -> {
        try {
          sleep(1500);
          confirmed = false;
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }).start();
      ci.setReturnValue(!itemStack.isEmpty());
    } else {
      mc.inGameHud.setOverlayMessage(Text.of(""), false);
      confirmed = false;
      itemStack = mc.player.getInventory().dropSelectedItem(entireStack);
      mc.player.playSound(SoundEvents.ITEM_BUNDLE_DROP_CONTENTS, 1.0F, 1.0F);
      mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(action, BlockPos.ORIGIN, Direction.DOWN));
      ci.setReturnValue(!itemStack.isEmpty());
    }
  }
}
