package xyz.pupbrained.drop_confirm;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "drop_confirm")
public class DropConfirmConfig implements ConfigData {
  @ConfigEntry.Category("general")
  @Comment("The delay in milliseconds before cancelling the drop.")
  public int confirmationResetDelay = 1500;
}
