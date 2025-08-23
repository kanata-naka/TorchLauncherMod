package jp.atelier_kanata.torchlaunchermod.registry;

import jp.atelier_kanata.torchlaunchermod.TorchLauncherMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class TorchLauncherModCreativeModeTabs {
  public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TorchLauncherMod.MODID);

  public static final DeferredHolder<CreativeModeTab, CreativeModeTab> HOLOLIVE_MOD_TAB =
      CREATIVE_MODE_TABS.register(TorchLauncherMod.MODID, () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.torch_launcher_mod"))
          .icon(() -> TorchLauncherModItems.TORCH_LAUNCHER.get().getDefaultInstance()).displayItems((parameters, output) -> {
            TorchLauncherModItems.ITEMS.getEntries().forEach((entry) -> output.accept(entry.get()));
          }).build());
}
