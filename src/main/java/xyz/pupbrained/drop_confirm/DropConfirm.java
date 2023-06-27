package xyz.pupbrained.drop_confirm;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DropConfirm implements ModInitializer {
  public static final Logger LOGGER = LoggerFactory.getLogger("DropConfirm");
  public static boolean confirmed = false;

  @Override
  public void onInitialize(ModContainer mod) {
    AutoConfig.register(DropConfirmConfig.class, JanksonConfigSerializer::new);

    LOGGER.info("DropConfirm initialized");
  }
}
