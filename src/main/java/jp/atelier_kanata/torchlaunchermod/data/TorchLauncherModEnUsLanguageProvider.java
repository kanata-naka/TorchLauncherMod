package jp.atelier_kanata.torchlaunchermod.data;

import jp.atelier_kanata.torchlaunchermod.TorchLauncherMod;
import jp.atelier_kanata.torchlaunchermod.registry.TorchLauncherModEntities;
import jp.atelier_kanata.torchlaunchermod.registry.TorchLauncherModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class TorchLauncherModEnUsLanguageProvider extends LanguageProvider {

  public TorchLauncherModEnUsLanguageProvider(PackOutput output) {
    super(output, TorchLauncherMod.MOD_ID, "en_us");
  }

  @Override
  protected void addTranslations() {
    add("torchlaunchermod.configuration.section.torchlaunchermod.common.toml.title", "Torch Launcher Mod Configuration");
    add("torchlaunchermod.configuration.launchable_blocks", "Launchable blocks");
    add("torchlaunchermod.configuration.launchable_blocks.tooltip", "List of blocks that can be fired from tourch launchers.");
    add("torchlaunchermod.configuration.launchable_blocks.button", "Edit");
    add("itemGroup.torchlaunchermod", "Torch Launcher Mod");
    add(TorchLauncherModItems.TORCH_LAUNCHER.get(), "Torch Launcher");
    add(TorchLauncherModEntities.TORCH_LAUNCHER_PROJECTILE_ENTITY.get(), "Torch Launcher Projectile");
  }

}
