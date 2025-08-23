package jp.atelier_kanata.torchlaunchermod;

import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your
// config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
  private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

  // a list of strings that are treated as resource locations for items
  public static final ModConfigSpec.ConfigValue<List<? extends String>> LAUNCHABLE_ITEMS = BUILDER.comment("List of items that can be fired from tourch launchers.")
      .defineList("launchable_items", List.of("minecraft:torch", "minecraft:soul_torch"), () -> "", Config::validateItemName);

  static final ModConfigSpec SPEC = BUILDER.build();

  private static boolean validateItemName(final Object obj) {
    return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
  }
}
