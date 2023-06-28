package xyz.pupbrained.drop_confirm.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.config.ConfigEntry;
import dev.isxander.yacl3.config.ConfigInstance;
import dev.isxander.yacl3.config.GsonConfigInstance;
import dev.isxander.yacl3.gui.controllers.BooleanController;
import dev.isxander.yacl3.gui.controllers.slider.DoubleSliderController;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.quiltmc.loader.api.QuiltLoader;
import xyz.pupbrained.drop_confirm.Util;

import java.util.List;
import java.util.function.Function;

public final class DropConfirmConfig {
  public static final ConfigInstance<DropConfirmConfig> INSTANCE =
    GsonConfigInstance
      .createBuilder(DropConfirmConfig.class)
      .setPath(QuiltLoader.getConfigDir().resolve("drop_confirm.json"))
      .build();

  @ConfigEntry
  public boolean enabled = true;
  @ConfigEntry
  public boolean playSounds = true;
  @ConfigEntry
  public double confirmationResetDelay = 1.0;

  public static Screen createScreen(Screen parent) {
    return YetAnotherConfigLib.create(INSTANCE, ((defaults, config, builder) -> {
      var defaultCategoryBuilder = ConfigCategory.createBuilder()
        .name(Text.translatable("category.drop_confirm.general"));

      var enabled = createOption(
        "option.drop_confirm.enabled",
        "option.drop_confirm.enabled.description",
        defaults.enabled,
        new Util.Mutable<>(config.enabled),
        booleanOption -> new BooleanController(booleanOption, true)
      );

      var playSound = createOption(
        "option.drop_confirm.play_sounds",
        "option.drop_confirm.play_sounds.description",
        defaults.playSounds,
        new Util.Mutable<>(config.playSounds),
        booleanOption -> new BooleanController(booleanOption, true)
      );

      var confirmationResetDelay = createOption(
        "option.drop_confirm.confirmation_reset_delay",
        "option.drop_confirm.confirmation_reset_delay.description",
        defaults.confirmationResetDelay,
        new Util.Mutable<>(config.confirmationResetDelay),
        doubleOption -> new DoubleSliderController(doubleOption, 0.0, 10.0, 0.1)
      );

      return builder
        .title(Text.translatable("config.drop_confirm.title"))
        .category(
          defaultCategoryBuilder
            .options(List.<Option<?>>of(enabled, playSound, confirmationResetDelay))
            .build()
        );
    })).generateScreen(parent);
  }

  private static <T> Option<T> createOption(
    String optionName,
    String optionDescription,
    T defaultValue,
    Util.Mutable<T> configValue,
    Function<Option<T>, Controller<T>> controllerProvider
  ) {
    return Option.<T>createBuilder()
      .name(Text.translatable(optionName))
      .description(
        OptionDescription.createBuilder()
          .text(Text.translatable(optionDescription))
          .build()
      )
      .binding(defaultValue, configValue::getValue, configValue::setValue)
      .customController(controllerProvider)
      .build();
  }

}
