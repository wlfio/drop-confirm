package xyz.pupbrained.drop_confirm;

import net.minecraft.client.network.ClientPlayerEntity;
import xyz.pupbrained.drop_confirm.config.DropConfirmConfig;

public class Util {
  public static boolean confirmed = false;

  public static boolean isDropConfirmDisabled(DropConfirmConfig config) {
    return !config.enabled;
  }

  public static boolean isMainHandStackEmpty(ClientPlayerEntity player) {
    return player.getInventory().getMainHandStack().isEmpty();
  }

  public static final class Mutable<T> {
    private T value;

    public Mutable(T value) {
      this.value = value;
    }

    public T getValue() {
      return value;
    }

    public void setValue(T value) {
      this.value = value;
    }
  }
}
