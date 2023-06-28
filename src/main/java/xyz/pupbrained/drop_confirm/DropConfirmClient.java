package xyz.pupbrained.drop_confirm;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBind;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents;
import xyz.pupbrained.drop_confirm.config.DropConfirmConfig;

public class DropConfirmClient implements ClientModInitializer {
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
        var config = DropConfirmConfig.INSTANCE.getConfig();
        config.enabled = !config.enabled;
        DropConfirmConfig.INSTANCE.save();
        MinecraftClient.getInstance().inGameHud.setOverlayMessage(
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
