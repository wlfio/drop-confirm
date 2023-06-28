package xyz.pupbrained.drop_confirm.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "drop_confirm")
public class DropConfirmConfig implements ConfigData {
  @Comment("Whether or not to enable DropConfirm")
  public boolean enabled = true;

  @Comment("Whether or not to play a sound upon dropping an item")
  public boolean playSound = true;

  @Comment("The delay in milliseconds before cancelling the drop")
  public int confirmationResetDelay = 1500;
}
