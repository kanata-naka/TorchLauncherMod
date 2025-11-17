package jp.atelier_kanata.torchlaunchermod.data;

import java.util.concurrent.CompletableFuture;
import jp.atelier_kanata.torchlaunchermod.TorchLauncherMod;
import jp.atelier_kanata.torchlaunchermod.registry.TorchLauncherModItems;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class TorchLauncherModItemTagsProvider extends ItemTagsProvider {

  public TorchLauncherModItemTagsProvider(PackOutput output, CompletableFuture<Provider> lookupProvider,
      CompletableFuture<TagLookup<Block>> blockTagsProvider, ExistingFileHelper existingFileHelper) {
    super(output, lookupProvider, blockTagsProvider, TorchLauncherMod.MOD_ID, existingFileHelper);
  }

  @Override
  protected void addTags(Provider provider) {
    this.tag(ItemTags.DURABILITY_ENCHANTABLE).add(TorchLauncherModItems.TORCH_LAUNCHER.get());
  }

}