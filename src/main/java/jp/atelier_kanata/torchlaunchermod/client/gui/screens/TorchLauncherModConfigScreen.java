package jp.atelier_kanata.torchlaunchermod.client.gui.screens;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import jp.atelier_kanata.torchlaunchermod.TorchLauncherMod;
import jp.atelier_kanata.torchlaunchermod.config.TorchLauncherModConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.DropdownBoxEntry;
import me.shedaniel.clothconfig2.gui.entries.NestedListListEntry;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class TorchLauncherModConfigScreen {

  public static Screen create(Screen parent) {
    final ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent).setTitle(
        Component.translatable(TorchLauncherMod.MOD_ID + ".configuration.section.torchlaunchermod.common.toml.title"));

    ConfigCategory general = builder.getOrCreateCategory(Component.empty());
    ConfigEntryBuilder entryBuilder = builder.entryBuilder();

    List<Block> currentLaunchableBlockList = TorchLauncherModConfig.LAUNCHABLE_BLOCKS.get().stream() //
        .map(itemName -> Block.byItem(BuiltInRegistries.ITEM.getValue(ResourceLocation.parse(itemName)))) //
        .collect(Collectors.toCollection(ArrayList::new));

    List<Block> availableBlockList = BuiltInRegistries.ITEM.stream() //
        .map((item) -> Block.byItem(item)) //
        .filter((block) -> block != Blocks.AIR) //
        .sorted(Comparator.comparing(Block::toString)).collect(Collectors.toList());

    general.addEntry(new NestedListListEntry<Block, DropdownBoxEntry<Block>>(
        Component.translatable(TorchLauncherMod.MOD_ID + ".configuration.launchable_blocks"), // fieldName
        currentLaunchableBlockList, // value
        true, // defaultExpand
        () -> Optional.of(new Component[] {
            Component.translatable(TorchLauncherMod.MOD_ID + ".configuration.launchable_blocks.tooltip") }), // tooltipSupplier
        value -> TorchLauncherModConfig.LAUNCHABLE_BLOCKS
            .set(value.stream().map(block -> block.asItem().toString()).collect(Collectors.toList())), // saveConsumer
        () -> currentLaunchableBlockList, // defaultValue
        entryBuilder.getResetButtonKey(), // resetButtonKey
        true, // deleteButtonEnabled
        false, // insertInFront
        (element, nestedListListEntry) -> { // createNewCell
          Block defaultBlock = element != null ? element : Blocks.TORCH;
          return entryBuilder
              .startDropdownMenu(Component.empty(),
                  DropdownMenuBuilder.TopCellElementBuilder.ofBlockObject(defaultBlock),
                  DropdownMenuBuilder.CellCreatorBuilder.ofBlockObject())
              .setDefaultValue(defaultBlock)
              //
              .setSelections(availableBlockList)
              //
              .build();
        }));

    return builder.setSavingRunnable(() -> {
      TorchLauncherModConfig.SPEC.save();
    }).build();
  }

}
