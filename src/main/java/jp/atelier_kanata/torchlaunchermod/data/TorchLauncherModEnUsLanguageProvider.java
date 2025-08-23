package jp.atelier_kanata.torchlaunchermod.data;

import jp.atelier_kanata.torchlaunchermod.TorchLauncherMod;
import jp.atelier_kanata.torchlaunchermod.registry.TorchLauncherModEntities;
import jp.atelier_kanata.torchlaunchermod.registry.TorchLauncherModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class TorchLauncherModEnUsLanguageProvider extends LanguageProvider {

  public TorchLauncherModEnUsLanguageProvider(PackOutput output) {
    super(output, TorchLauncherMod.MODID, "en_us");
  }

  @Override
  protected void addTranslations() {
    add("itemGroup.torch_launcher_mod", "Torch Launcher Mod");
    
    add("torchlaunchermod.configuration.launchable_items", "Launchable items");

    add(TorchLauncherModItems.TORCH_LAUNCHER.get(), "Torch Launcher");

    add(TorchLauncherModEntities.TORCH_LAUNCHER_PROJECTILE_ENTITY.get(), "Torch Launcher Projectile");
  }

}
