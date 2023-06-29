package xyz.pupbrained;

import net.minecraft.client.network.ClientPlayerEntity;
import xyz.pupbrained.config.DropConfirmConfig;

public final class Util {
  public static boolean confirmed = false;

  public static boolean isDisabled(DropConfirmConfig config) {
    return !config.enabled;
  }

  public static boolean isMainHandStackEmpty(ClientPlayerEntity player) {
    return player.getInventory().getMainHandStack().isEmpty();
  }
}
