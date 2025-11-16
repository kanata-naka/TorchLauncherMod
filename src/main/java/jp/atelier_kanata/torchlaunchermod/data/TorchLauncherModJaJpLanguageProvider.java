package jp.atelier_kanata.torchlaunchermod.data;

import jp.atelier_kanata.torchlaunchermod.TorchLauncherMod;
import jp.atelier_kanata.torchlaunchermod.registry.TorchLauncherModEntities;
import jp.atelier_kanata.torchlaunchermod.registry.TorchLauncherModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class TorchLauncherModJaJpLanguageProvider extends LanguageProvider {

  public TorchLauncherModJaJpLanguageProvider(PackOutput output) {
    super(output, TorchLauncherMod.MOD_ID, "ja_jp");
  }

  @Override
  protected void addTranslations() {
    add("torchlaunchermod.configuration.section.torchlaunchermod.common.toml.title", "トーチランチャーMOD 設定");
    add("torchlaunchermod.configuration.launchable_blocks", "発射可能なブロック");
    add("torchlaunchermod.configuration.launchable_blocks.tooltip", "トーチランチャーから発射可能なブロックを設定します");
    add("torchlaunchermod.configuration.launchable_blocks.button", "編集");
    add("itemGroup.torchlaunchermod", "トーチランチャーMOD");
    add(TorchLauncherModItems.TORCH_LAUNCHER.get(), "トーチランチャー");
    add(TorchLauncherModEntities.TORCH_LAUNCHER_PROJECTILE_ENTITY.get(), "トーチランチャーの発射物");
  }
}
