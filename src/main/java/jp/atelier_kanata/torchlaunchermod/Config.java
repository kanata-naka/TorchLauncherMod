package jp.atelier_kanata.torchlaunchermod;

import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {

  private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

  public static final ModConfigSpec.ConfigValue<List<? extends String>> LAUNCHABLE_BLOCKS = BUILDER.comment("List of blocks that can be fired from tourch launchers.")
      .defineList("launchable_blocks", List.of("minecraft:torch", "minecraft:soul_torch"), () -> "", Config::validateItemName);

  static final ModConfigSpec SPEC = BUILDER.build();

  private static boolean validateItemName(final Object obj) {
    if (obj instanceof String itemName) {
      ResourceLocation resourceLocation = ResourceLocation.parse(itemName);
      return BuiltInRegistries.ITEM.containsKey(resourceLocation) && Block.byItem(BuiltInRegistries.ITEM.getValue(resourceLocation)) != Blocks.AIR;
    }
    return false;
  }

}
