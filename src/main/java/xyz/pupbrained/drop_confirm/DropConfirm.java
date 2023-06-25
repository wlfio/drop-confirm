package xyz.pupbrained.drop_confirm;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DropConfirm implements ModInitializer {
  public static final Logger LOGGER = LoggerFactory.getLogger("DropConfirm");

  @Override
  public void onInitialize(ModContainer mod) {
    LOGGER.info("Initialized!");
  }
}
