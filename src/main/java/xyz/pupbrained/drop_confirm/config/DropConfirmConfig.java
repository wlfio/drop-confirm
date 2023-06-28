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

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class DropConfirmConfig {
  private static final class Mutable<T> {
    T value;

    Mutable(T value) {
      this.value = value;
    }
  }

  public static final ConfigInstance<DropConfirmConfig> INSTANCE =
    GsonConfigInstance
      .createBuilder(DropConfirmConfig.class)
      .setPath(QuiltLoader.getConfigDir().resolve("drop_confirm.json"))
      .build();

  @ConfigEntry
  public boolean enabled = true;

  @ConfigEntry
  public boolean playSound = true;

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
        new Mutable<>(config.enabled),
        booleanOption -> new BooleanController(booleanOption, true)
      );

      var playSound = createOption(
        "option.drop_confirm.play_sound",
        "option.drop_confirm.play_sound.description",
        defaults.playSound,
        new Mutable<>(config.playSound),
        booleanOption -> new BooleanController(booleanOption, true)
      );

      var confirmationResetDelay = createOption(
        "option.drop_confirm.confirmation_reset_delay",
        "option.drop_confirm.confirmation_reset_delay.description",
        defaults.confirmationResetDelay,
        () -> config.confirmationResetDelay,
        val -> config.confirmationResetDelay = val,
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
    Mutable<T> configValue,
    Function<Option<T>, Controller<T>> controllerProvider
  ) {
    return Option.<T>createBuilder()
      .name(Text.translatable(optionName))
      .description(buildOptionDescription(optionDescription))
      .binding(defaultValue, () -> configValue.value, val -> configValue.value = val)
      .customController(controllerProvider)
      .build();
  }

  private static <T> Option<T> createOption(
    String optionName,
    String optionDescription,
    T defaultValue,
    Supplier<T> getter,
    Consumer<T> setter,
    Function<Option<T>, Controller<T>> controllerProvider
  ) {
    return Option.<T>createBuilder()
      .name(Text.translatable(optionName))
      .description(buildOptionDescription(optionDescription))
      .binding(defaultValue, getter, setter)
      .customController(controllerProvider)
      .build();
  }

  private static OptionDescription buildOptionDescription(String textKey) {
    return OptionDescription.createBuilder()
      .text(Text.translatable(textKey))
      .build();
  }
}
