package xyz.pupbrained.drop_confirm;

import me.shedaniel.autoconfig.AutoConfig;
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
  public static KeyBind toggleKey;

  @Override
  public void onInitializeClient(ModContainer mod) {
    toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBind(
      "key.drop_confirm.toggle",
      GLFW.GLFW_KEY_UNKNOWN,
      "category.drop_confirm.keybinds"
    ));

    ClientTickEvents.END.register(client -> {
      while (toggleKey.wasPressed()) {
        DropConfirmConfig config = AutoConfig.getConfigHolder(DropConfirmConfig.class).getConfig();
        config.enabled = !config.enabled;
        AutoConfig.getConfigHolder(DropConfirmConfig.class).save();
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
