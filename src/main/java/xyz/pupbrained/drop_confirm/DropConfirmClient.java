package xyz.pupbrained.drop_confirm;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBind;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents;
import xyz.pupbrained.drop_confirm.config.DropConfirmConfig;

import java.util.Objects;

public final class DropConfirmClient implements ClientModInitializer {
  @Override
  public void onInitializeClient(ModContainer mod) {
    DropConfirmConfig.INSTANCE.load();

    var toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBind(
      "key.drop_confirm.toggle",
      GLFW.GLFW_KEY_UNKNOWN,
      "category.drop_confirm.keybinds"
    ));

    ClientTickEvents.END.register(client -> {
      while (toggleKey.wasPressed()) {
        var mc = MinecraftClient.getInstance();
        var config = DropConfirmConfig.INSTANCE.getConfig();
        var player = Objects.requireNonNull(mc.player);

        config.enabled = !config.enabled;

        DropConfirmConfig.INSTANCE.save();

        if (config.playSounds)
          player.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1.0f, config.enabled ? 1.0f : 0.5f);

        mc.inGameHud.setOverlayMessage(
          Text
            .literal("DropConfirm: ")
            .append(
              Text
                .literal(config.enabled ? "ON" : "OFF")
                .formatted(config.enabled ? Formatting.GREEN : Formatting.RED)
            ),
          false
        );
      }
    });
  }
}
