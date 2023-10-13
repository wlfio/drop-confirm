package xyz.pupbrained.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.gui.controllers.BooleanController;
import dev.isxander.yacl3.gui.controllers.slider.DoubleSliderController;
import dev.isxander.yacl3.platform.YACLPlatform;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class DropConfirmConfig {
  public static final ConfigClassHandler<DropConfirmConfig> GSON = ConfigClassHandler.createBuilder(DropConfirmConfig.class)
    .serializer(config -> GsonConfigSerializerBuilder.create(config)
      .setPath(YACLPlatform.getConfigDir().resolve("drop_confirm.json"))
      .build())
    .build();

  @SerialEntry
  public boolean enabled = true;

  @SerialEntry
  public boolean playSounds = true;

  @SerialEntry
  public double confirmationResetDelay = 1.0;

  public static Screen createScreen(Screen parent) {
    return YetAnotherConfigLib.create(DropConfirmConfig.GSON, ((defaults, config, builder) -> {
      var defaultCategoryBuilder = ConfigCategory.createBuilder()
        .name(Text.translatable("category.drop_confirm.general"));

      var enabled = createOption(
        "option.drop_confirm.enabled",
        "option.drop_confirm.enabled.description",
        defaults.enabled,
        () -> config.enabled,
        val -> config.enabled = val,
        booleanOption -> new BooleanController(booleanOption, true)
      );

      var playSounds = createOption(
        "option.drop_confirm.play_sounds",
        "option.drop_confirm.play_sounds.description",
        defaults.playSounds,
        () -> config.playSounds,
        val -> config.playSounds = val,
        booleanOption -> new BooleanController(booleanOption, true)
      );

      var confirmationResetDelay = createOption(
        "option.drop_confirm.confirmation_reset_delay",
        "option.drop_confirm.confirmation_reset_delay.description",
        defaults.confirmationResetDelay,
        () -> config.confirmationResetDelay,
        val -> config.confirmationResetDelay = val,
        doubleOption -> new DoubleSliderController(doubleOption, 1.0, 5.0, 0.05)
      );

      return builder
        .title(Text.translatable("config.drop_confirm.title"))
        .category(
          defaultCategoryBuilder
            .options(List.of(enabled, playSounds, confirmationResetDelay))
            .build()
        );
    })).generateScreen(parent);
  }

  private static <T> Option<T> createOption (
    String name,
    String description,
    T defaultValue,
    Supplier<T> currentValue,
    Consumer<T> newValue,
    Function<Option<T>, Controller<T>> customController
  ) {
    return Option.<T>createBuilder()
      .name(Text.translatable(name))
      .description(
        OptionDescription.createBuilder()
          .text(Text.translatable(description))
          .build()
      )
      .binding(defaultValue, currentValue, newValue)
      .customController(customController)
      .build();
  }
}

