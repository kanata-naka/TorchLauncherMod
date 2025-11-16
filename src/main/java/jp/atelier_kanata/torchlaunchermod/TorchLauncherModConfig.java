package jp.atelier_kanata.torchlaunchermod;

import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.ModConfigSpec;

public class TorchLauncherModConfig {

  private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

  public static final ModConfigSpec.ConfigValue<List<? extends String>> LAUNCHABLE_BLOCKS =
      BUILDER.defineList("launchable_blocks", List.of("minecraft:torch", "minecraft:soul_torch"), () -> "", TorchLauncherModConfig::validateBlockItemName);

  public static final ModConfigSpec SPEC = BUILDER.build();

  private static boolean validateBlockItemName(Object value) {
    return value instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName))
        && Block.byItem(BuiltInRegistries.ITEM.getValue(ResourceLocation.parse(itemName))) != Blocks.AIR;
  }

}
